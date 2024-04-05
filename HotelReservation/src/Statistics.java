import java.util.ArrayList;
import java.util.HashMap;
public class Statistics {
    private final int K;
    private final HashMap <Integer, Integer> hotelRoomAmount;
    private ArrayList<HandledRequest> lastHandledRequests;
    private final ArrayList<HandledRequest> allHandledRequests;
    private int currentRevenue;
    private int currentRoomOccupation;
    private HashMap<Integer, Integer> currentEveryRoomOccupation;
    private HashMap<Integer, HashMap<Integer, Integer>> currentOccupation;
    private int acceptedRequestsCount;
    private int declinedRequestsCount;
    private int currentTime;
    private int maxLoad;
    private HashMap<Integer, Integer> roomTypeFactRequestCount;
    private final HashMap<Integer, ArrayList<Integer>> loadCounts;


    public Statistics(int K, HashMap<Integer, Integer> hotelRoomAmount, HashMap<Integer, HashMap<Integer, Integer>> currentOccupation) {
        this.lastHandledRequests = new ArrayList<>();
        this.allHandledRequests = new ArrayList<>();
        this.currentRevenue = 0;
        this.currentRoomOccupation = 0;
        this.currentEveryRoomOccupation = new HashMap<>();
        this.currentOccupation = currentOccupation;
        this.acceptedRequestsCount = 0;
        this.declinedRequestsCount = 0;
        this.currentTime = 0;
        this.K = K;
        this.hotelRoomAmount = hotelRoomAmount;
        this.maxLoad = 0;
        this.roomTypeFactRequestCount = new HashMap<>();
        this.loadCounts = new HashMap<>();

    }
    public void update(ArrayList<HandledRequest> handledRequests, int curIncome, int currentRoomOccupation,
                       HashMap<Integer, Integer> currentEveryRoomOccupation,
                       HashMap<Integer, HashMap<Integer, Integer>> currentOccupation,
                       HashMap<Integer, Integer> roomTypeFactRequestCount,
                       int acceptedRequestsCount, int currentTime) {
        this.lastHandledRequests = handledRequests;
        this.allHandledRequests.addAll(handledRequests);
        this.currentRevenue += curIncome;
        this.currentRoomOccupation = currentRoomOccupation;
        this.currentEveryRoomOccupation = currentEveryRoomOccupation;
        this.currentOccupation = currentOccupation;
        this.acceptedRequestsCount += acceptedRequestsCount;
        this.declinedRequestsCount += (handledRequests.size() - acceptedRequestsCount);
        this.currentTime = currentTime;
        this.maxLoad = Math.max(this.maxLoad, this.getOverallOccupation());
        this.roomTypeFactRequestCount = roomTypeFactRequestCount;
        addLoads(currentEveryRoomOccupation);
    }

    public ArrayList<HandledRequest> getLastHandledRequests() {
        return this.lastHandledRequests;
    }


    public int getCurrentRevenue() {
        return this.currentRevenue;
    }

    public HashMap<Integer, HashMap<Integer, Integer>> getCurrentOccupation() {
        return this.currentOccupation;
    }

    public int getAcceptedRequestsCount() {
        return this.acceptedRequestsCount;
    }

    public String getCurrentTimeToString() {
        return this.currentTime / 24 + "д - " + this.currentTime % 24 + "ч";
    }

    public int getCurrentTime() {
        return this.currentTime;
    }

    public int getOverallOccupation() {
        return this.currentRoomOccupation * 100 / this.K;
    }

    public int getAcceptedPercentage() {
        return (this.acceptedRequestsCount * 100) / (acceptedRequestsCount + declinedRequestsCount);
    }

    public int getAllRequestsCount() {
        return this.acceptedRequestsCount + this.declinedRequestsCount;
    }


    public  HashMap<Integer, Integer> getEveryTypeAverageLoad() {
        HashMap <Integer, Integer> everyTypeLoad = new HashMap<>();
        int sum;
        for (int key: this.loadCounts.keySet()) {
            sum = 0;
            for (int i = 0; i < this.loadCounts.get(key).size(); i++) {
                sum += this.loadCounts.get(key).get(i);
            }
            everyTypeLoad.put(key, sum / this.loadCounts.get(key).size());
        }
        return everyTypeLoad;
    }

    public static int getOverallLAverageLoad(HashMap<Integer, Integer> everyTypeAverageLoad) {
        int value = 0;
        for (int key: everyTypeAverageLoad.keySet()) {
            value += everyTypeAverageLoad.get(key);
        }
        return value / everyTypeAverageLoad.size();
    }

    private void addLoads(HashMap <Integer, Integer> currentEveryRoomOccupation) {
        for (int key: currentEveryRoomOccupation.keySet()) {
            if (hotelRoomAmount.get(key) != 0) {
                if (!loadCounts.containsKey(key)) {
                    loadCounts.put(key, new ArrayList<>());
                }
                loadCounts.get(key).add((currentEveryRoomOccupation.get(key) * 100) / hotelRoomAmount.get(key));
            }
        }
    }

    public HashMap<Integer, Integer> getRoomTypeFactRequestCount() {
        return this.roomTypeFactRequestCount;
    }
}
