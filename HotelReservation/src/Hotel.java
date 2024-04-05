import java.util.ArrayList;
import java.util.HashMap;

public class Hotel {
    private final HashMap<Integer, ArrayList<HotelRoom>> hotelRooms;
    public Hotel(HashMap<Integer, ArrayList<HotelRoom>> hotelRooms) {
        this.hotelRooms = hotelRooms;
    }

    public HashMap<Integer, ArrayList<HotelRoom>> getHotelRooms() {
        return this.hotelRooms;
    }

    /*
    Добавляет бронь (startDate, endDate) в номер типа hotelRoomType, который находится
    в hotelRooms по ключу hotelRoomType и индексу idx.
    Возвращает id забронированного номера.
    Подразумевается, что такая бронь корректна.
     */
    public int addDateToRoom(int hotelRoomType, int idx, int startDate, int endDate) {
        this.hotelRooms.get(hotelRoomType).get(idx).addDate(startDate, endDate);
        return this.hotelRooms.get(hotelRoomType).get(idx).getId();
    }

    public HashMap<Integer, HashMap<Integer, Integer>> countOccupation(int currentTime) {
        HashMap <Integer, HashMap<Integer, Integer>> currentOccupation = new HashMap<>();
        for (int key: this.hotelRooms.keySet()) {
            HashMap<Integer, Integer> curMap = new HashMap<>();
            currentOccupation.put(key, curMap);
            for (HotelRoom hotelRoom: this.hotelRooms.get(key)) {
                curMap.put(hotelRoom.getId(), hotelRoom.occupied(currentTime));
            }
        }
        //System.out.println(this.hotelRooms);
        return currentOccupation;
    }
}
