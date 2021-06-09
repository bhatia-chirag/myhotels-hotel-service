# Reservation
* Reservation object
  ```
  {
    reservation_details: {
                          bookingId: string
                          hotel: {
                                  name: string
                                  location: string
                                 }
                          roomType: string
                          dates: {
                                  start: date
                                  end: date
                                 }
                          paymentType: string
                          guest: {
                                  name: string
                                  phone: integer
                                 }
                         }
  }
  ```
**POST /reservation/:payment_type/hotel/:name/:room_type/date?start=""&end=""**
----
  Creates reservation based on specified values.
* **URL Params**  
  *Required:* `payment_type=[string]`
  *Required:* `name=[string]`
  *Required:* `room_type=[string]`
  *Required:* `start=[date]`
  *Required:* `end=[date]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:**  
* **Code:** 200  
  **Content:**  `<reservation_object>`
* **Error Response:**
  * **Code:** 404  
  **Content:** `{ error : "Hotel doesn't exist" }`  
  OR  
  * **Code:** 401  
  **Content:** `{ error : "Invalid request. Please check date format." }`
  OR  
  * **Code:** 403  
  **Content:** `{ error : "You are not authorized to perform this request." }`

**POST /reservation/guest/:name/:phone/:add/:payment_type/hotel/:name/:room_type/date?start=""&end=""**
----
  Creates reservation based on specified values and returns reservation details after success.
* **URL Params**  
  *Required:* `payment_type=[string]`
  *Required:* `name=[string]`
  *Required:* `room_type=[string]`
  *Required:* `start=[date]`
  *Required:* `end=[date]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:**  
* **Code:** 200  
  **Content:**  `<reservation_object>`
* **Error Response:**
  * **Code:** 404  
  **Content:** `{ error : "Hotel doesn't exist" }`  
  OR  
  * **Code:** 401  
  **Content:** `{ error : "Invalid request. Please check date format." }`
  OR  
  * **Code:** 403  
  **Content:** `{ error : "You are not authorized to perform this request." }`
  
**GET /reservation/:id**
----
  Returns the specified reservation detail.
* **URL Params**  
  *Required:* `id=[string]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
  Authorization: Bearer `<JWT Token>`
* **Success Response:** 
* **Code:** 200  
  **Content:**  `{ <reservation_object> }` 
* **Error Response:**  
  * **Code:** 404  
  **Content:** `{ error : "Reservation id not found." }`
  OR
  * **Code:** 403
  **Content:** `{ error : "You are not authorized to perform this operation." }`

**PUT /reservation/:id?params="new value"**
----
  Edits the specified reservation based on new params value specified and returns updated reservation details.
* **URL Params**  
  *Required:* `id=[string]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
  Authorization: Bearer `<JWT Token>`
* **Success Response:** 
* **Code:** 200  
  **Content:**  `{ <reservation_object> }` 
* **Error Response:**  
  * **Code:** 404  
  **Content:** `{ error : "Reservation id not found." }`
  OR
  * **Code:** 403
  **Content:** `{ error : "You are not authorized to perform this operation." }`
  OR
  * **Code:** 401
  **Content:** `{ error : "Invalid parameter values." }`

**DELETE /reservation/:id**
----
  Cancels booking based on the specified id.
* **URL Params**  
  *Required:* `id=[string]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
  Authorization: Bearer `<JWT Token>`
* **Success Response:** 
* **Code:** 204  
  **Content:**
  None
* **Error Response:**  
  * **Code:** 401  
  **Content:** `{ error : "You are not authorized to perform this operation." }`
