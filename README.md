# Parcel Delivery System #

![Travis (.org)](https://img.shields.io/travis/isopropylcyanide/Jwt-Spring-Security-JPA)
![GitHub](https://img.shields.io/github/license/isopropylcyanide/Jwt-Spring-Security-JPA?color=blue)


## API Gateway ##
- Netflix Zuul      - `git checkout master`
- KONG API gateway  - `git checkout kong-api-gateway`
- Spring Cloud Gateway  - `git checkout spring-cloud-gateway`


## Getting Started ##

- `start.sh` - start app
- `stop.sh`  - stop app
- `http://localhost:9000/` - Kafdrop (Kafka UI)


## Architecture ##
![parcel-delivery-system](https://user-images.githubusercontent.com/6951427/141544464-3fe7d483-d36c-4ec9-b019-19823ab6b367.png)


## Swagger Docs: ##
```
http://localhost:8080/ or http://localhost:8080/swagger-ui/
```

### Ms-Identity Rest API ###
![ms-identity-api](https://user-images.githubusercontent.com/6951427/141534398-4f225aaf-1dda-42a6-bc77-0bb447602336.jpg)

### Ms-Order Rest API ###
![ms-order-api](https://user-images.githubusercontent.com/6951427/141534822-d215f375-712b-4ed9-bebc-0f6de7158de5.jpg)

### Ms-Delivery Rest API ###
![ms-delivery-api](https://user-images.githubusercontent.com/6951427/141534898-ea3f3190-6a9a-4d6d-8b7a-570d279e8d12.jpg)


# User stories: #


## USER: ##
<br />

<details>
<summary>Can create an user account</summary>

```
curl --location --request POST 'http://localhost:8080/identity/auth/signup' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "mushfiqazeri@mail.ru"
    "password": "12345678",
    "firstName": "Mushfiq",
    "lastName": "Mammadov",
    "phone": "+994555906952",
}'
```
</details>

---

<details>
<summary>Can log in as user</summary>

```
curl --location --request POST 'http://localhost:8080/identity/auth/signin' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "mushfiqazeri@mail.ru"
    "password": "12345678",
}'

Response:
{
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtdXNoZmlxYXplcmlAbWFpbC5ydSIsImF1dGgiOiJST0xFX1VTRVIsY2FuY2VsX29yZGVyLGNoYW5nZV9vcmRlcl9kZXN0aW5hdGlvbixjcmVhdGVfb3JkZXIsdmlld19vcmRlcix2aWV3X3VzZXIiLCJ0b2tlbl90eXBlIjoiQUNDRVNTIiwiZnVsbF9uYW1lIjoiTXVzaGZpcSBNYW1tYWRvdiIsInVzZXJfdHlwZSI6IlVTRVIiLCJleHAiOjE2MzY3NDU0MTV9.MGWf174DaLzHdEJTJ9FKrv_F8zj3eGqQBZ3ypmbdYstdN0iLKEMpdofMWnU_JZiVnzvDWSab9dHrW6uchU49zg",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtdXNoZmlxYXplcmlAbWFpbC5ydSIsImF1dGgiOiJST0xFX1VTRVIsY2FuY2VsX29yZGVyLGNoYW5nZV9vcmRlcl9kZXN0aW5hdGlvbixjcmVhdGVfb3JkZXIsdmlld19vcmRlcix2aWV3X3VzZXIiLCJ0b2tlbl90eXBlIjoiUkVGUkVTSCIsImZ1bGxfbmFtZSI6Ik11c2hmaXEgTWFtbWFkb3YiLCJ1c2VyX3R5cGUiOiJVU0VSIiwiZXhwIjoxNjM2NzQ4MTE1fQ.X90941BnZ9oi2FlqtqTQfRFK8W8kpR2JHSCkbQQ-uQj8cW8fAUSaWREkmSfR14O3eTWga5CiwbtYtGG6urRLrg"
}
```
</details>

---

<details>
<summary>Can create a parcel delivery order</summary>

```
curl --location --request POST 'http://localhost:8080/order/api/orders' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {jwt}' \
--data-raw '{
    "destination": "Jafar Jabbarli 44",
    "name": "Short info of parcel",
    "note": "Some notes for delivery company"
}'

Response:
{
    "id": 2,
    "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
    "name": "Short info of parcel",
    "destination": "Jafar Jabbarli 44",
    "note": "Some notes for delivery company",
    "status": "INITIAL",
    "weight": null,
    "amount": null,
    "courier": null,
    "createdBy": "mushfiqazeri@mail.ru",
    "createdAt": "2021-11-12T19:28:00.503",
    "updatedAt": "2021-11-12T19:28:00.503"
}
```
</details>

---

<details>
<summary>Can change the destination of a parcel delivery order</summary>

```
curl --location --request PUT 'http://localhost:8080/order/api/orders/change-destination' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {jwt}' \
--data-raw '{
    "orderId": 2
    "newDestination": "Nasimi district, Bulbul avenue 60",
}'

Response:
{
    "id": 2,
    "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
    "name": "Short info of parcel",
    "destination": "Nasimi district, Bulbul avenue 60",
    "note": "Some notes for delivery company",
    "status": "INITIAL",
    "weight": null,
    "amount": null,
    "courier": null,
    "createdBy": "mushfiqazeri@mail.ru",
    "createdAt": "2021-11-12T19:28:00.503",
    "updatedAt": "2021-11-12T19:28:00.503"
}
```
</details>

---

<details>
<summary>Can cancel a parcel delivery order</summary>

```
curl --location --request PUT 'http://localhost:8080/order/api/orders/cancel/2' \
--header 'Authorization: Bearer {jwt}'
```
</details>

---

<details>
<summary>Can see the details of a delivery</summary>

```
GET http://localhost:8080/order/api/orders/1

Response:
{
    "id": 1,
    "orderNumber": "e845a3e6-0468-4c2b-bb46-85468d57228c",
    "name": "Short info of parcel",
    "destination": "Nasimi district, Bulbul avenue 60",
    "note": "Some notes for delivery company",
    "status": "CANCEL",
    "weight": null,
    "amount": null,
    "courier": null,
    "createdBy": "mushfiqazeri@mail.ru",
    "createdAt": "2021-11-11T19:48:14.37",
    "updatedAt": "2021-11-11T19:54:23.756"
}
```
</details>

---

<details>
<summary>Can see all parcel delivery orders that he/she created</summary>

```
curl --location --request GET 'http://localhost:8080/order/api/orders' \
--header 'Authorization: Bearer {jwt}'

Response:
[
    {
        "id": 1,
        "orderNumber": "e845a3e6-0468-4c2b-bb46-85468d57228c",
        "name": "Short info of parcel",
        "destination": "Nasimi district, Bulbul avenue 60",
        "note": "Some notes for delivery company",
        "status": "DELIVERED",
        "weight": 0.16,
        "amount": 0.8,
        "courier": "name.surname@email.com",
        "createdBy": "mushfiqazeri@mail.ru",
        "createdAt": "2021-11-11T19:48:14.37",
        "updatedAt": "2021-11-11T21:00:37.616"
    },
    {
        "id": 2,
        "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
        "name": "Short info of parcel",
        "destination": "Nasimi district, Bulbul avenue 60",
        "note": "Some notes for delivery company",
        "status": "CANCEL",
        "weight": null,
        "amount": null,
        "courier": null,
        "createdBy": "mushfiqazeri@mail.ru",
        "createdAt": "2021-11-12T19:28:00.503",
        "updatedAt": "2021-11-12T19:31:35.354"
    }
]
```
</details>

<br />

## ADMIN: ##
<br />

<details>
<summary>Login as admin</summary>

```
curl --location --request POST 'http://localhost:8080/identity/auth/signin' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {jwt}' \
--data-raw '{
    "username": "mushfiqazeri@gmail.com"
    "password": "test1234",
}'

Response:
{
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtdXNoZmlxYXplcmlAZ21haWwuY29tIiwiYXV0aCI6IlJPTEVfQURNSU4sYWNjZXB0X29yZGVyLGFkZF9wZXJtaXNzaW9uLGFkZF9yb2xlLGFkZF91c2VyLGFzc2lnbl9jb3VyaWVyLGNhbmNlbF9vcmRlcixjaGFuZ2Vfb3JkZXJfc3RhdHVzLGNoYW5nZV9wYXJjZWxfc3RhdHVzLGNyZWF0ZV9jb3VyaWVyLGRlbGV0ZV9wZXJtaXNzaW9uLGRlbGV0ZV9yb2xlLGRlbGV0ZV91c2VyLHRyYWNrX3BhcmNlbCx1cGRhdGVfb3JkZXIsdXBkYXRlX3BhcmNlbCx1cGRhdGVfcGVybWlzc2lvbix1cGRhdGVfcm9sZSx1cGRhdGVfdXNlcix2aWV3X2NvdXJpZXIsdmlld19vcmRlcix2aWV3X3BhcmNlbCx2aWV3X3Blcm1pc3Npb24sdmlld19yb2xlLHZpZXdfdXNlcix2aWV3X3VzZXJfbGlzdCIsInRva2VuX3R5cGUiOiJBQ0NFU1MiLCJmdWxsX25hbWUiOiJNdXNoZmlxIE1hbW1hZG92IiwidXNlcl90eXBlIjoiQURNSU4iLCJleHAiOjE2MzY3NDY2MTR9.FbvMhRXCp_r_plefIyNLE_gzmHQP72_H_s6cEJNcJ9mKBpAj8A03AIy3mJvBD9Yw7K4MzNT0kMbMW9WcBcrsIA",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtdXNoZmlxYXplcmlAZ21haWwuY29tIiwiYXV0aCI6IlJPTEVfQURNSU4sYWNjZXB0X29yZGVyLGFkZF9wZXJtaXNzaW9uLGFkZF9yb2xlLGFkZF91c2VyLGFzc2lnbl9jb3VyaWVyLGNhbmNlbF9vcmRlcixjaGFuZ2Vfb3JkZXJfc3RhdHVzLGNoYW5nZV9wYXJjZWxfc3RhdHVzLGNyZWF0ZV9jb3VyaWVyLGRlbGV0ZV9wZXJtaXNzaW9uLGRlbGV0ZV9yb2xlLGRlbGV0ZV91c2VyLHRyYWNrX3BhcmNlbCx1cGRhdGVfb3JkZXIsdXBkYXRlX3BhcmNlbCx1cGRhdGVfcGVybWlzc2lvbix1cGRhdGVfcm9sZSx1cGRhdGVfdXNlcix2aWV3X2NvdXJpZXIsdmlld19vcmRlcix2aWV3X3BhcmNlbCx2aWV3X3Blcm1pc3Npb24sdmlld19yb2xlLHZpZXdfdXNlcix2aWV3X3VzZXJfbGlzdCIsInRva2VuX3R5cGUiOiJSRUZSRVNIIiwiZnVsbF9uYW1lIjoiTXVzaGZpcSBNYW1tYWRvdiIsInVzZXJfdHlwZSI6IkFETUlOIiwiZXhwIjoxNjM2NzQ5MzE0fQ.3DzQEPYsvFqyRZI_KlWs_cMK1SEZN-8QbZn3K71EKWliWUtMky5BTLnBUIc7GK40R92dUjBL47vabczzCItB3w"
}
```
</details>

---

<details>
<summary>Can change the status of a parcel delivery order</summary>

```
curl --location --request PUT 'http://localhost:8080/order/api/orders/change-status' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {jwt}' \
--data-raw '{
    "orderId": 2
    "newOrderStatus": "INITIAL",
}'

Response:
{
    "id": 2,
    "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
    "name": "Short info of parcel",
    "destination": "Nasimi district, Bulbul avenue 60",
    "note": "Some notes for delivery company",
    "status": "INITIAL",
    "weight": null,
    "amount": null,
    "courier": null,
    "createdBy": "mushfiqazeri@mail.ru",
    "createdAt": "2021-11-12T19:28:00.503",
    "updatedAt": "2021-11-12T19:31:35.354"
}
```
</details>

---

<details>
<summary>Can view all parcel delivery orders</summary>

```
curl --location --request GET 'http://localhost:8080/order/api/orders' \
--header 'Authorization: Bearer {jwt}'

Response:
[
    {
        "id": 1,
        "orderNumber": "e845a3e6-0468-4c2b-bb46-85468d57228c",
        "name": "Short info of parcel",
        "destination": "Nasimi district, Bulbul avenue 60",
        "note": "Some notes for delivery company",
        "status": "DELIVERED",
        "weight": 0.16,
        "amount": 0.8,
        "courier": "name.surname@email.com",
        "createdBy": "mushfiqazeri@mail.ru",
        "createdAt": "2021-11-11T19:48:14.37",
        "updatedAt": "2021-11-11T21:00:37.616"
    },
    {
        "id": 2,
        "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
        "name": "Short info of parcel",
        "destination": "Nasimi district, Bulbul avenue 60",
        "note": "Some notes for delivery company",
        "status": "INITIAL",
        "weight": null,
        "amount": null,
        "courier": null,
        "createdBy": "mushfiqazeri@mail.ru",
        "createdAt": "2021-11-12T19:28:00.503",
        "updatedAt": "2021-11-12T19:42:01.455"
    }
]
```
</details>

---

<details>
<summary>Accept order and determine some details (parcel weight, delivery amount etc)</summary>

```
curl --location --request PUT 'http://localhost:8080/order/api/orders/accept/2' \
--header 'Authorization: Bearer {jwt}'

{
    "id": 2,
    "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
    "name": "Short info of parcel",
    "destination": "Nasimi district, Bulbul avenue 60",
    "note": "Some notes for delivery company",
    "status": "ACCEPTED",
    "weight": 0.332,
    "amount": 1.66,
    "courier": null,
    "createdBy": "mushfiqazeri@mail.ru",
    "createdAt": "2021-11-12T19:28:00.503",
    "updatedAt": "2021-11-12T19:42:01.455"
}
```
</details>

---

<details>
<summary>Create a courier account</summary>

```
curl --location --request POST 'http://localhost:8080/identity/auth/signup/courier' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {jwt}' \
--data-raw '{
    "username": "name.surname@email.com"
    "password": "77778888",
    "firstName": "Jhon",
    "lastName": "Mikayil",
    "phone": "+994506600101",
}'
```
</details>

---

<details>
<summary>Can assign parcel delivery order to courier</summary>

```
curl --location --request PUT 'http://localhost:8080/order/api/orders/assign-courier' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {jwt}' \
--data-raw '{
    "orderId": 2
    "courierUsername": "name.surname@email.com",
}'

Response:
{
    "id": 2,
    "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
    "name": "Short info of parcel",
    "destination": "Nasimi district, Bulbul avenue 60",
    "note": "Some notes for delivery company",
    "status": "PENDING",
    "weight": 0.332,
    "amount": 1.66,
    "courier": "name.surname@email.com",
    "createdBy": "mushfiqazeri@mail.ru",
    "createdAt": "2021-11-12T19:28:00.503",
    "updatedAt": "2021-11-12T19:43:59.778"
}
```
</details>

---

<details>
<summary>Can track the delivery order by coordinates</summary>

```
curl --location --request GET 'http://localhost:8080/delivery/api/parcels/track/2' \
--header 'Authorization: Bearer {jwt}'

Response:
40.4403340:49.8075000
```
</details>

---

<details>
<summary>Can see list of couriers with their statuses</summary>

```
curl --location --request GET 'http://localhost:8080/identity/api/users/couriers' \
--header 'Authorization: Bearer {jwt}'

Response:
[
    {
        "id": 4,
        "username": "name.surname@email.com",
        "firstName": "Jhon",
        "lastName": "Mikayil",
        "type": "COURIER",
        "phone": "+994506600101",
        "enabled": true,
        "createdAt": "2021-11-11T20:23:49.144",
        "updatedAt": "2021-11-11T20:23:49.144",
        "roles": [
            {
                "id": 3,
                "name": "COURIER"
            }
        ]
    }
]
```
</details>

<br />

## Courier: ##
<br />

<details>
<summary>Can log in</summary>

```
curl --location --request POST 'http://localhost:8080/identity/auth/signin' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer {jwt}' \
--data-raw '{
    "username": "name.surname@email.com"
    "password": "77778888",
}'

Response:
{
    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuYW1lLnN1cm5hbWVAZW1haWwuY29tIiwiYXV0aCI6IlJPTEVfQ09VUklFUixjb21wbGV0ZV9wYXJjZWxfb3JkZXIscmVjZWl2ZV9wYXJjZWwsdmlld19wYXJjZWwiLCJ0b2tlbl90eXBlIjoiQUNDRVNTIiwiZnVsbF9uYW1lIjoiSmhvbiBNaWtheWlsIiwidXNlcl90eXBlIjoiQ09VUklFUiIsImV4cCI6MTYzNjc0Nzg1OH0.Uo9TqPWpZJ-m8pi5a7SnVKxaU3_bbltyE0hLIr3en8izoGzRX9-N9oDv_-ijAgH6prM6UR7U4xIMKd_Vr2Oa-g",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuYW1lLnN1cm5hbWVAZW1haWwuY29tIiwiYXV0aCI6IlJPTEVfQ09VUklFUixjb21wbGV0ZV9wYXJjZWxfb3JkZXIscmVjZWl2ZV9wYXJjZWwsdmlld19wYXJjZWwiLCJ0b2tlbl90eXBlIjoiUkVGUkVTSCIsImZ1bGxfbmFtZSI6Ikpob24gTWlrYXlpbCIsInVzZXJfdHlwZSI6IkNPVVJJRVIiLCJleHAiOjE2MzY3NTA1NTh9.97HIdcHLV14L0MlTibNKTontQEGIDYSWWGCWxQmi5BTblDd-Wm2FgcY4Be6tbDRc_W0tZCr6UgxaSmUQxi-7wQ"
}
```
</details>

---

<details>
<summary>Can view all parcel delivery orders that assigned to him</summary>

```
curl --location --request GET 'http://localhost:8080/delivery/api/parcels' \
--header 'Authorization: Bearer {jwt}'

Response:
[
    {
        "id": 1,
        "orderNumber": "e845a3e6-0468-4c2b-bb46-85468d57228c",
        "destination": "Nasimi district, Bulbul avenue 60",
        "coordination": "40.4403340:49.8075000",
        "status": "DELIVERED",
        "courier": "name.surname@email.com",
        "createdAt": "2021-11-11T20:25:19.488",
        "updatedAt": "2021-11-11T21:00:37.608"
    },
    {
        "id": 2,
        "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
        "destination": "Nasimi district, Bulbul avenue 60",
        "coordination": "40.4403340:49.8075000",
        "status": "PENDING",
        "courier": "name.surname@email.com",
        "createdAt": "2021-11-12T19:52:34.662",
        "updatedAt": "2021-11-12T19:52:34.662"
    }
]
```
</details>

---

<details>
<summary>Can see the details of a delivery order</summary>

```
curl --location --request GET 'http://localhost:8080/delivery/api/parcels/2' \
--header 'Authorization: Bearer {jwt}'

Response:
{
    "id": 2,
    "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
    "destination": "Nasimi district, Bulbul avenue 60",
    "coordination": "40.4403340:49.8075000",
    "status": "PENDING",
    "courier": "name.surname@email.com",
    "createdAt": "2021-11-12T19:52:34.662",
    "updatedAt": "2021-11-12T19:52:34.662"
}
```
</details>

---

<details>
<summary>Receive parcel order by courier (Status change from PENDING to IN_PROGRESS)</summary>

```
curl --location --request PUT 'http://localhost:8080/delivery/api/parcels/receive-parcel/2' \
--header 'Authorization: Bearer {jwt}'

Response:
{
    "id": 2,
    "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
    "destination": "Nasimi district, Bulbul avenue 60",
    "coordination": "40.4403340:49.8075000",
    "status": "IN_PROGRESS",
    "courier": "name.surname@email.com",
    "createdAt": "2021-11-12T19:52:34.662",
    "updatedAt": "2021-11-12T19:52:34.662"
}
```
</details>

---

<details>
<summary>Parcel order is completed by courier (Status change from IN_PROGRESS to DELIVERED)</summary>

```
curl --location --request PUT 'http://localhost:8080/delivery/api/parcels/complete-order/2' \
--header 'Authorization: Bearer {jwt}'

{
    "id": 2,
    "orderNumber": "f5cecb23-e26c-4c22-8406-17ea4551fae3",
    "destination": "Nasimi district, Bulbul avenue 60",
    "coordination": "40.4403340:49.8075000",
    "status": "DELIVERED",
    "courier": "name.surname@email.com",
    "createdAt": "2021-11-12T19:52:34.662",
    "updatedAt": "2021-11-12T20:00:07.063"
}
```
</details>

<br />


