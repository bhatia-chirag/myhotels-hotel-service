# Guest
* Guest Object
```
{
  name: string
  address: string
  phoneNumber: integer
  registerLoyalty: boolean
  creditCard: integer
}
```
**POST /guests/add**
----
  Adds guest information from body.
* **URL Params**  
  None
* **Data Params**  `{<guest_object>}`
* **Headers**  
  Content-Type: application/json  
* **Success Response:**  
* **Code:** 201  
  **Content:** 
    None
* **Error Response:**  
  * **Code:** 401  
  **Content:** `{ error : "Invalid request. Please check sent format." }`
  
**PUT /guests/edit/:phone?params="new value"**
----
  Updates field of the specified guest and returns updated object.
* **URL Params**
  *Required:* `phone=[integer]`
* **Headers**
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:**
* **Code:** 200
  **Content:**  `{ <guest_object> }`  
* **Error Response:**
  * **Code:** 404  
  **Content:** `{ error : "Guest doesn't exist" }`  
  OR  
  * **Code:** 403  
  **Content:** `{ error : "You are unauthorized to make this request." }`

**GET /guests/:phone**
----
  Returns specified guest.
  * **URL Params**
  *Required:* `phone=[integer]`
* **Headers**
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:**
* **Code:** 200
  **Content:**  `{ <guest_object> }`  
* **Error Response:**
  * **Code:** 404  
  **Content:** `{ error : "Guest doesn't exist" }`  
  OR  
  * **Code:** 403  
  **Content:** `{ error : "You are unauthorized to make this request." }`

**GET /guests/**
----
  Returns all guests in system.
  * **URL Params**
  None
* **Headers**
  Content-Type: application/json  
  Authorization: Bearer `<JWT Token>`
* **Success Response:**
* **Code:** 200
  **Content:**  
```
{
  guests: [
            {<guest_object>},
            {<guest_object>},
            {<guest_object>}
          ]
}
```
* **Error Response:**
  * **Code:** 403  
  **Content:** `{ error : "You are unauthorized to make this request." }`

**DELETE /guests/remove/:phone**
----
  Removes specified guest data except data required for auditing.
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
  **Content:** `{ error : "Guest doesn't exist" }`  
  OR  
  * **Code:** 403  
  **Content:** `{ error : "You are unauthorized to make this request." }`
  
**POST /guests/login/**
----
* **URL Params**
  None
* **Data Params** 
  None
* **Headers**
  Content-Type: application/json
* **Success Response:**
  * **Code:** 200
  **Content:** `{ jwt: <JWT Token> }`
* **Error Response:** 
  * **Code:** 403  
  **Content:** `{ error : "Incorrect username or password." }`
