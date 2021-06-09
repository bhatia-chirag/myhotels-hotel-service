#Hotels
* Room object
```
{
  roomType: string
  total: integer
}
```
* Hotel object
```
{
  name: string
  description: string
  location: string
  rooms: [
          {<room_object>},
          {<room_object>}
         ]
}
```
**GET /hotels**
----
  Returns all active hotels in the system.
* **URL Params**  
  None
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json  
* **Success Response:**  
* **Code:** 200  
  **Content:**  
```
{
  hotels: [
           {<hotel_object>},
           {<hotel_object>},
           {<hotel_object>}
         ]
}
```

**GET /hotels/:active**
----
  Returns all hotels based on specified active value.
* **URL Params**
  *Required:* `active=[boolean]`
* **Data Params**
  None
* **Headers**
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:**
* **Code:** 200
  **Content:**
```
{
  hotels: [
           {<hotel_object>},
           {<hotel_object>},
           {<hotel_object>}
         ]
}
```
* **Error Response:**

**GET /hotels/:name**
----
  Returns the specified hotel.
* **URL Params**  
  *Required:* `hotel=[string]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:** 
* **Code:** 200  
  **Content:**  `{ <hotel_object> }` 
* **Error Response:**  
  * **Code:** 403  
  **Content:** `{ error : "Invalid request. Please check date format." }`

**GET /hotels/:name/date/:start/:end**
----
  Returns all room types availability associated with the specified hotel.
* **URL Params**  
  *Required:* `hotel=[string]`
  *Required:* `start=[datetime(iso 8601)]`
  *Required:* `end=[datetime(iso 8601)]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json  
* **Success Response:**  
* **Code:** 200  
  **Content:**  
```
{
  orders: [
           {<order_object>},
           {<order_object>},
           {<order_object>}
         ]
}
```
* **Error Response:**  
  * **Code:** 404  
  **Content:** `{ error : "Hotel doesn't exist" }`  
  OR  
  * **Code:** 403  
  **Content:** `{ error : "Invalid request. Please check date format." }`

**GET /hotels/:name/:room-type/date/:start/:end**
----
  Returns all room types availability associated with the specified hotel.
* **URL Params**  
  *Required:* `hotel=[string]`
  *Required:* `room-type=[string]`
  *Required:* `start=[datetime(iso 8601)]`
  *Required:* `end=[datetime(iso 8601)]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json  
* **Success Response:**  
* **Code:** 200  
  **Content:**  
```
{
  orders: [
           {<order_object>},
           {<order_object>},
           {<order_object>}
         ]
}
```
* **Error Response:**  
  * **Code:** 404  
  **Content:** `{ error : "Hotel doesn't exist" }`  
  OR
  * **Code:** 404  
  **Content:** `{ error : "Room type doesn't exist for specified hotel." }`  
  OR  
  * **Code:** 403  
  **Content:** `{ error : "Invalid request. Please check date format." }`

**POST /hotels/add**
----
  Creates a new Hotel and returns the new object.
* **URL Params**  
  None
* **Headers**  
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Data Params**  `{ <hotel_object> }` 
* **Success Response:**  
* **Code:** 200  
  **Content:**  `{ <hotel_object> }` 
* **Error Response:**  
  * **Code:** 403  
  **Content:** 
```
  { 
    error : "Invalid request.",
    reason: <reason>
  }
```  
  OR
  * **Code:** 401  
  **Content:** `{ error : "You are unauthorized to make this request." }`

**PUT /hotels/:name?param="new_value"**
----
  Updates fields of the specified hotel and returns the updated object.
* **URL Params**  
  *Required:* `name=[string]`
* **Headers**  
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:** 
* **Code:** 200  
  **Content:**  `{ <hotel_object> }`  
* **Error Response:**  
  * **Code:** 404  
  **Content:** `{ error : "Hotel doesn't exist" }`  
  OR  
  * **Code:** 401  
  **Content:** `{ error : "You are unauthorized to make this request." }`

**DELETE /hotels/:name**
----
  Deactivates the specified hotel.
* **URL Params**  
  *Required:* `name=[string]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:** 
  * **Code:** 204 
* **Error Response:**  
  * **Code:** 404  
  **Content:** `{ error : "Hotel doesn't exist" }`  
  OR  
  * **Code:** 401  
  **Content:** `{ error : "You are unauthorized to make this request." }`