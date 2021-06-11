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
* Availability object
```
{
  roomType: string
  availability: integer
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
    * **Code:** 403  
      **Content:** `{ error : "You are not authorized to perform this request." }`
      OR
    * **Code:** 404  
      **Content:** `{ error : "Hotel not found for specified value." }`
      OR
    * **Code:** 400  
      **Content:** `{ error : "Invalid request. Please check the request and try again." }`

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
    * **Code:** 404  
      **Content:** `{ error : "Hotel not found for specified value." }`
      OR
    * **Code:** 400  
      **Content:** `{ error : "Invalid request. Please check date format." }`

**GET /hotels/:name/date/:start/:end**
----
Returns all room types availability associated with the specified hotel.
* **URL Params**  
  *Required:* `hotel=[string]`
  *Required:* `start=[date(iso 8601)]`
  *Required:* `end=[date(iso 8601)]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**
```
{
  availabilities: [
           {<availablity_object>},
           {<availablity_object>},
           {<availablity_object>}
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
  *Required:* `start=[date(iso 8601)]`
  *Required:* `end=[date(iso 8601)]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**
```
{
  availabilities: [
           {<availablity_object>},
           {<availablity_object>},
           {<availablity_object>}
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

**POST /hotels/login**
----
Checks the credentials and issues a JWT token.
* **URL Params**
  None
* **Headers**
  None
* **Data Params**
  None
* **Success Response:**
* **Code:** 200
  **Content:**
```
  {
    jwt: string
  }
```
* **Error Response:**
* **Code:** 403  
  **Content:** `{ error : "Invalid username or password." }`
  OR
* **Code:** 400
  **Content:** `{ error: "Invalid request format. Please check the values and try again." }`

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
* **Code:** 401  
  **Content:**
```
  { 
    error : "Invalid request.",
    reason: <reason>
  }
```  
  OR
* **Code:** 403  
  **Content:** `{ error : "You are unauthorized to make this request." }`

**PUT /hotels/:name?param="new_value"**
----
Update fields of the specified hotel and returns the updated object.
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
    * **Code:** 403  
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
    * **Code:** 403  
      **Content:** `{ error : "You are unauthorized to make this request." }`

**POST /hotels/:name/:room-type/date/:start/:end**
----
Creates and updates records for room availability associated with the specified hotel.
* **URL Params**  
  *Required:* `hotel=[string]`
  *Required:* `room-type=[string]`
  *Required:* `start=[datetime(iso 8601)]`
  *Required:* `end=[datetime(iso 8601)]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:**
* **Code:** 200  
  **Content:** `{ success }`
* **Error Response:**
    * **Code:** 404  
      **Content:** `{ error : "Hotel doesn't exist" }`  
      OR
    * **Code:** 404  
      **Content:** `{ error : "Room type doesn't exist for specified hotel." }`  
      OR
    * **Code:** 401  
      **Content:** `{ error : "Invalid request. Please check date format." }`
      OR
    * **Code:** 401  
      **Content:** `{ error : "Oops! Selected room type is not available anymore on these dates. Please check the dates and hotel availability and try again." }`
      OR
    * **Code:** 403  
      **Content:** `{ error : "You are unauthorized to make this request." }`