public class Request {
    private final int hotelRoomType;
    private final int startDate;
    private final int endDate;
    private final int requestDate;
    public Request(int hotelRoomType, int startDate, int endDate, int requestDate) {
        this.hotelRoomType = hotelRoomType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requestDate = requestDate;
    }

    @Override
    public String toString() {
        return  "RoomType: " + this.hotelRoomType + "  " +
                "startDate: " + this.startDate / 24 + "d, " + this.startDate % 24 + "h  " +
                "endDate: " + this.endDate / 24 + "d, " + this.endDate % 24 + "h  " +
                "requestDate: " + this.requestDate / 24 + "d, " + this.requestDate % 24 + "h\n";
    }

    public int getHotelRoomType() {
        return this.hotelRoomType;
    }

    public int getStartDate() {
        return this.startDate;
    }

    public int getEndDate() {
        return this.endDate;
    }

    public int getRequestDate() {
        return this.requestDate;
    }
}
