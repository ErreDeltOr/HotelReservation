//Объект данного класса является обработааной заявкой. К уже имеющимся
//полям заявки (Request) добавляются поля isAccepted (одобрена/принята ли заявка),
//actualRoomType (Если итоговый выделенный тип номера не совпадает с типом в
//исходной заявке), id - физический id комнаты выделенной по данной заявке
public class HandledRequest {
    private final int hotelRoomType;
    private final int startDate;
    private final int endDate;
    private final int requestDate;
    private final boolean isAccepted;
    private final int actualRoomType;
    private final int id;

    public HandledRequest(Request request, boolean isAccepted, int actualRoomType, int id) {
        this.hotelRoomType = request.getHotelRoomType();
        this.startDate = request.getStartDate();
        this.endDate = request.getEndDate();
        this.requestDate = request.getRequestDate();
        this.isAccepted = isAccepted;
        this.actualRoomType = actualRoomType;
        this.id = id;
    }

    @Override
    public String toString() {
        String accepted = "ДА";
        String factRoomType = String.valueOf(this.actualRoomType);
        String roomId = String.valueOf(this.id);
        if (!this.isAccepted) {
            accepted = "НЕТ";
            factRoomType = "НЕТ";
            roomId = "НЕТ";
        }
        return  "____" + this.hotelRoomType + "_____|" +
                "_" + this.startDate / 24 + "д-" + this.startDate % 24 + "ч_|" +
                "_" + this.endDate / 24 + "д-" + this.endDate % 24 + "ч_|" +
                "____" + this.requestDate / 24 + "д-" + this.requestDate % 24 + "ч___|" +
                "____" + accepted + "___|" +
                "_____" + factRoomType + "_____|" +
                "___" + roomId + "__";
    }

}