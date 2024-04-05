import java.util.ArrayList;
import java.util.Arrays;

public class HotelRoom {
    private final int id;
    private final int hotelRoomType;
    private final ArrayList <ArrayList<Integer>> occupancyList;

    public HotelRoom(int id, int hotelRoomType, ArrayList<ArrayList<Integer>> occupancyList) {
        this.id = id;
        this.hotelRoomType = hotelRoomType;
        this.occupancyList = occupancyList;
    }

    /*
    Добавляет бронь (startDate, endDate) в номер.
    Подразумевается, что такая бронь корректна.
     */
    public void addDate(int startDate, int endDate) {
        int idx = 0;
        while (idx < this.occupancyList.size() && startDate > this.occupancyList.get(idx).get(1)) {
            idx++;
        }
        if (idx == this.occupancyList.size()) {
            occupancyList.add(new ArrayList<>(Arrays.asList(startDate, endDate)));
        }
        else {
            occupancyList.add(idx, new ArrayList<>(Arrays.asList(startDate, endDate)));
        }
    }

    public int occupied(int currentTime) {
        int idx = 0;
        while (idx < this.occupancyList.size()) {
            if ((this.occupancyList.get(idx).get(0) <= currentTime) && (currentTime < this.occupancyList.get(idx).get(1))) {
                return 1;
            }
            idx++;
        }
        return 0;
    }

    public ArrayList<ArrayList<Integer>> getOccupancyList() {
        return this.occupancyList;
    }

    public int getId() {
        return this.id;
    }
}
