namespace java es.udc.ws.app.thrift

struct ThriftExcursionDto {
	1: i64 excursionId;
	2: string city;
	3: string description;
	4: string date;
	5: double price;
	6: i16 maxParticipants;
	7: i16 numFree;
}

struct ThriftReservationDto{
    1: i64 excursionId;
    2: i64 reservationId;
    3: string userEmail;
    4: string creditCardNumber;
    5: string registerDate;
    6: i16 numParticipants;
    7: double price;
    8: string canceled;
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftExcursionLateDateException {
    1: i64 excursionId
}

exception ThriftExcursionNotEnoughPlacesException {
    1: i64 excursionId
}

exception ThriftReservationOutOfTimeException {
    1: i64 reservationId
    2: string proposalDate
}

exception ThriftReservationNotEnoughPlacesException {
    1: i64 reservationId
    2: i32 places

}

exception ThriftReservationCanceledException {
    1: i64 reservationId
    2: string proposalDate
}

exception ThriftReservationNotSameUserEmailException{
    1: i64 reservationId
    2: string userEmail
}

exception ThriftReservationNotPossibleException{
    1: i64 reservationId;
    2: string proposalDate;
}

service ThriftExcursionService {

  ThriftExcursionDto addExcursion(1: ThriftExcursionDto excursionDto) throws (1: ThriftInputValidationException e)

  void updateExcursion(1: ThriftExcursionDto excursion) throws (1: ThriftInputValidationException e1,
   2: ThriftInstanceNotFoundException e2, 3: ThriftExcursionLateDateException e3,
   4: ThriftExcursionNotEnoughPlacesException e4)

   list<ThriftExcursionDto> findExcursions(1: string city, 2: string earlyDate, 3:string lateDate)
   throws (1:ThriftInputValidationException e)

    i64 buyReservation(1: i64 excursionId, 2: string userEmail, 3: string creditCardNumber, 4: i32 numParticipants)
    throws (1: ThriftInputValidationException e, 2: ThriftInstanceNotFoundException e2, 3:ThriftReservationOutOfTimeException e3,
    ThriftReservationNotEnoughPlacesException e4)

    void cancelReservation(1: i64 reservationId, 2: string userEmail)
    throws (1: ThriftInstanceNotFoundException e1, 2: ThriftReservationNotPossibleException e2,
    3: ThriftReservationOutOfTimeException e3, 4: ThriftReservationCanceledException e4,
    5: ThriftReservationNotSameUserEmailException e5)

    list<ThriftReservationDto> findReservations(1: string userEmail)

}