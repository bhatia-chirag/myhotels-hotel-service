import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return BookingDto on successful booking"
    request {
        method 'POST'
        url '/hotels/booking/123456'
        body(
                hotelName: "Myhotel1",
                roomType: "queen",
                startDate: "2021-06-29",
                endDate: "2021-07-01"
        )
        headers {
            contentType(applicationJson())
        }
    }
    response {
        status 200
        body(
                hotelName: "Myhotel1",
                roomType: "queen",
                startDate: "2021-06-29",
                endDate: "2021-07-01"
        )
        headers {
            contentType(applicationJson())
        }
    }
}