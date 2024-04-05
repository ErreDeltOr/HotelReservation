import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Experiment {
    private final int K;
    private final int M;
    private final int minInterval;
    private final int maxInterval;
    private int currentTime;
    private final int stepSize;
    private final int discountValue;
    private final ArrayList<Request> requests;
    private int reqIdx;
    private final HashMap<Integer, Integer> hotelPrices;
    private final HashMap<Integer, Integer> hotelRoomAmount;
    private final RequestHandleSystem system;
    private final Random random = new Random();
    private int currentRevenue;
    private final Statistics statistics;
    private final HashMap<Integer, Integer> roomTypeFactRequestCount;



    public Experiment(int K, int M, int minInterval, int maxInterval, int stepSize, int discountValue,
                      HashMap<Integer, Integer> hotelPrices, HashMap<Integer, Integer> hotelRoomAmount) {
        this.K = K;
        this.M = M;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        this.currentTime = 0;
        this.stepSize = stepSize;
        this.discountValue = discountValue;
        this.hotelRoomAmount = hotelRoomAmount;
        this.hotelPrices = hotelPrices;
        this.reqIdx = 0;
        this.currentRevenue = 0;
        this.requests = generateRequests(this.hotelRoomAmount);
        this.system = new RequestHandleSystem(this.makeRooms(), this.discountValue, this.hotelPrices, this.hotelRoomAmount);
        this.statistics = new Statistics(this.K, hotelRoomAmount, this.countOccupation(this.currentTime));
        this.roomTypeFactRequestCount = new HashMap<>();
        for (int key: this.hotelRoomAmount.keySet()) {
            if (hotelRoomAmount.get(key) != 0 ) {
                this.roomTypeFactRequestCount.put(key, 0);
            }
        }
    }
    public void makeRestSteps() {
        while (currentTime < M * 24) {
            this.makeStep();
        }
    }
    public void makeStep() {
        if (currentTime < M * 24) {
            this.currentTime += this.stepSize;
            if (currentTime > M * 24) {
                currentTime = M * 24;
            }
        }
        ArrayList <HandledRequest> handledRequests = new ArrayList<>();
        ArrayList<Integer> booked;
        int income = 0;
        int acceptedRequestsCount = 0;
        while (this.reqIdx < this.requests.size() && this.requests.get(this.reqIdx).getRequestDate() <= this.currentTime) {
            booked = this.system.handleRequest(this.requests.get(this.reqIdx));
            if (booked.get(0) != -1) {
                this.currentRevenue += booked.get(2); // Увеличиваем доход на цену
                income += booked.get(2);
                acceptedRequestsCount++;
                this.roomTypeFactRequestCount.put(booked.get(1), this.roomTypeFactRequestCount.get(booked.get(1)) + 1);
            }
            handledRequests.add(new HandledRequest(this.requests.get(this.reqIdx), (booked.get(0) != -1), booked.get(1), booked.get(0)));
            reqIdx++;
        }
        HashMap<Integer, HashMap<Integer, Integer>> currentOccupation = this.countOccupation(this.currentTime);
        HashMap<Integer, Integer> currentEveryRoomOccupation = this.countEveryRoomOccupation(currentOccupation);
        int currentRoomOccupation = this.countRoomOccupation(currentEveryRoomOccupation);
        this.statistics.update(handledRequests, income, currentRoomOccupation, currentEveryRoomOccupation,
                               currentOccupation, this.roomTypeFactRequestCount, acceptedRequestsCount, this.currentTime);
    }

    /*
    Заранее генерируются все заявки на заселение.
     */
    private ArrayList<Request> generateRequests(HashMap<Integer, Integer> hotelRoomAmount) {
        int reqType; // Тип заявки - сейчас (1), бронь (2).
        int requestDate = 0; // Дата получения заявки
        int startDate; // Дата начала проживания
        int endDate; // Дата конца проживания
        int hotelRoomType; // Тип отельного номера
        Request request;
        ArrayList<Request> reqs = new ArrayList<>();
        requestDate += this.random.nextInt(minInterval, maxInterval + 1);
        while (requestDate < M * 24) {
            reqType = this.random.nextInt(1, 3);
            if (reqType == 1) {           // Случай заселения сейчас
                startDate = requestDate;  //Дата заселения = дате заявки
                endDate = M * 24;         // Считаем, что дата выселения это дата конца периода моделирования
            }
            else {
                if (requestDate + 1 >= M * 24 - 1) {
                    break;
                }
                startDate = this.random.nextInt(requestDate + 1, M * 24 - 1);
                endDate = this.random.nextInt(startDate + 1, M * 24 + 1);
            }
            hotelRoomType = this.random.nextInt(1, 6);
            while (hotelRoomAmount.get(hotelRoomType) == 0) {
                hotelRoomType = this.random.nextInt(1, 6);
            }
            request = new Request(hotelRoomType, startDate, endDate, requestDate);
            reqs.add(request);
            requestDate += this.random.nextInt(minInterval, maxInterval + 1);
        }
        //System.out.println(reqs);
        return reqs;
    }

    /*
    Создаёт номера в отеле с тем количеством номеров определённого типа,
    которое ввёл пользователь.
     */
    private HashMap<Integer, ArrayList<HotelRoom>> makeRooms() {
        ArrayList<HotelRoom> oneTypeHotelRooms;
        HashMap<Integer, ArrayList<HotelRoom>> hotelRooms = new HashMap<>();
        HotelRoom hotelRoom;
        int id = 1;
        for (int key: hotelRoomAmount.keySet()) {
            oneTypeHotelRooms = new ArrayList<>();
            for (int j = 0; j < hotelRoomAmount.get(key); j++) {
                hotelRoom = new HotelRoom(id, key, new ArrayList<>());
                id++;
                oneTypeHotelRooms.add(hotelRoom);
            }
            hotelRooms.put(key, oneTypeHotelRooms);
        }
        return hotelRooms;
    }


    public Statistics getStatistics() {
        return this.statistics;
    }

    private HashMap<Integer, HashMap<Integer, Integer>> countOccupation(int currentTime) {
        return this.system.countOccupation(currentTime);
    }


    private HashMap<Integer, Integer> countEveryRoomOccupation(HashMap<Integer, HashMap<Integer, Integer>> currentOccupation) {
        HashMap<Integer, Integer> everyRoomOccupation = new HashMap<>();
        for (int key: currentOccupation.keySet()) {
            everyRoomOccupation.put(key, countRoomOccupation(currentOccupation.get(key)));
        }
        return everyRoomOccupation;
    }

    private int countRoomOccupation(HashMap<Integer, Integer> currentEveryRoomOccupation) {
        int value = 0;
        for (int key: currentEveryRoomOccupation.keySet()) {
            value += currentEveryRoomOccupation.get(key);
        }
        return value;
    }
}
