import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RequestHandleSystem {
    private final Hotel hotel;
    private final int discountValue;
    private final HashMap<Integer, Integer> hotelPrices;
    private final HashMap<Integer, Integer> hotelRoomAmount;
    public RequestHandleSystem(HashMap<Integer, ArrayList<HotelRoom>> rooms, int discountValue,
                               HashMap<Integer, Integer> hotelPrices,
                               HashMap<Integer, Integer> hotelRoomAmount) {
        this.discountValue = discountValue;
        this.hotelPrices = hotelPrices;
        this.hotelRoomAmount = hotelRoomAmount;
        this.hotel = new Hotel(rooms);
    }

    /*
    Бронирует комнату по некоторой стратегии. Если бронь невозможна, то
    возвращает -1, содержимое второго и третьего возвращаемого значения игнорируется.
    Если бронь возможна, то возвращает id забронированного номера, его тип и цену,
    по которой он был забронирован.
     */
    public ArrayList<Integer> handleRequest(Request request) {
        int minType = request.getHotelRoomType(); // Определяем минимальный класс (по удобству) номеров
        int idx = this.canBook(request.getStartDate(), request.getEndDate(), minType);
        while (idx == - 1 && minType <= 4) {
            minType += 1; // Если номеров нужного типа нет, подбираем номер большего комфорта со скидкой
            idx = this.canBook(request.getStartDate(), request.getEndDate(), minType);
        }

        int id = -1;
        if (idx != - 1) {
            id = this.hotel.addDateToRoom(minType, idx, request.getStartDate(), request.getEndDate());
        }
        int price = this.hotelPrices.get(request.getHotelRoomType());
        if (request.getHotelRoomType() != minType) {
            price = (discountValue * this.hotelPrices.get(minType)) / 100;
        }
        //System.out.println(request);
        return new ArrayList<>(Arrays.asList(id, minType, price * (request.getEndDate() - request.getStartDate()) / 24));
    }

    /*
    Возвращает порядковый номер гостиничного номера в списке номеров типа из Request,
    если бронь возможна. В противном случае возвращает -1.
     */
    private int canBook(int startDate, int endDate, int hotelRoomType) {
        int idx = 0;
        int roomNum = 0;
        boolean flag = false;
        HotelRoom room;
        for (int i = 0; i < this.hotelRoomAmount.get(hotelRoomType); i++) {
            room = this.hotel.getHotelRooms().get(hotelRoomType).get(i);
            flag = true;
            while (idx < room.getOccupancyList().size()) {
                if (endDate <= room.getOccupancyList().get(idx).get(0)) {
                    break;
                }
                else if (startDate >= room.getOccupancyList().get(idx).get(1)) {
                    idx++;
                }
                else {
                    flag = false;
                    break;
                }
            }
            roomNum = i;
            if (flag) {
                break;
            }
        }
        if (!flag) {
            return -1;
        }
        else {
            return roomNum;
        }
    }

    public HashMap<Integer, HashMap<Integer, Integer>> countOccupation(int currentTime) {
        return this.hotel.countOccupation(currentTime);
    }

}
