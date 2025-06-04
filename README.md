# Smartthings

Samsung's IoT solution platform that allows users worldwide to control their devices anytime and anywhere.
Using [Ratpack](https://ratpack.io/) Framework and Java 17 with SQL Server Database.

## Installation & Run

### Clone Project

Run command below in shell or terminal

```bash
git clone https://github.com/paskalhensa/samsung-test.git
```

### Run SQL Script

Execute the provided [SQL script](src/main/resources) on SQL Server

### Run the Project

This project was developed using Java 17 and requires Gradle to run. This project also requires the environment
variables below:\
`DB_URL`: JDBC connection URL to your SQL Server, example:
`jdbc:sqlserver://localhost:1433;databaseName=smartthings;trustServerCertificate=true`\
`DB_USER`: SQL Server username, example: `henry`\
`DB_PASSWORD`: SQL Server password, example: `password123!!!`\
`SECRET_KEY`: Secret key used for JWT signing (minimum 32 character), example: `non-est-ad-astra-mollis-e-terris-via`

#### Running with IntelliJ

1. Go to **Run > Edit Configurations**
2. Select your app
3. Add the required environment variables, for example:\
   `DB_PASSWORD=password123!!!;DB_URL=jdbc:sqlserver://localhost:
   1433\;databaseName=smartthings\;trustServerCertificate=true;DB_USER=henry;SECRET_KEY=non-est-ad-astra-mollis-e-terris-via`

#### Running via terminal

Go to your terminal and run the command below

```bash
DB_USER=henry \
DB_PASSWORD=password123!!! \
DB_URL="jdbc:sqlserver://localhost:1433;databaseName=smartthings;trustServerCertificate=true" \
SECRET_KEY=non-est-ad-astra-mollis-e-terris-via \
./gradlew run
```

## API Endpoints

All APIs require Bearer Token authentication and authorization except for Open Endpoints. Unauthenticated user will be
given the response below\
`401` Missing or invalid header

```json
{
  "success": false,
  "message": "Missing or invalid Authorization header"
}
```

`401` Expired token

```json
{
  "success": false,
  "message": "Expired token, please re-login."
}
```

Other than listed responses below, all APIs may also give this response if there is an uncaught exception in the
service.

```json
{
  "success": false,
  "message": "Something went wrong in internal server."
}
```

### Open Endpoints

#### /login

* `POST`: login to get auth token.
  Example request:

```json
{
  "username": "username",
  "password": "password"
}
```

Example response:\
`200` Login success

```json
{
  "success": true,
  "message": "Login Success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyY29iYWxhZ2kiLCJyb2xlIjoiY2xpZW50IiwidXNlcklkIjoxMiwiaWF0IjoxNzQ5MDI3NDU4LCJleHAiOjE3NDkwMzEwNTh9.RwxSe-Wz4IvTVYthVXn_kVtINycydWQXbgLkkaEaY-E"
  }
}
```

`401` Invalid Credentials

```json
{
  "success": false,
  "message": "Invalid credentials"
}
```

#### /register

* `POST`: register user to smartthings. Only for registering user (client) not for vendor or admin. Example request:

```json
{
  "username": "username",
  "password": "password",
  "name": "John Doe",
  "dob": "1999-01-01",
  "address": "123 Example Street, City",
  "country": "ID"
}
```

Validations:\
`username`: required, min length 3, max length 50\
`password`: required, min length 3, max length 72\
`name`: required, max length 100\
`dob`: required in `yyyy-MM-dd` format\
`address`: required\
`country`: required, ISO 3166-1 alpha-2 (e.g., "ID", "SG")\
Example response:
`201` User created

```json
{
  "success": true,
  "message": "User successfully created"
}
```

`400` Validation Error

```json
{
  "success": false,
  "message": "Validation failed when registering user",
  "errors": [
    "password: size must be between 3 and 72"
  ]
}
```

### Vendor Endpoints

Required role to access these endpoints is `vendor`. Users without the required role will be given the response below
`403`

```json
{
  "success": false,
  "message": "Forbidden"
}
```

#### /api/vendor/devices

* `POST`: create a new device. Example request:

```json
{
  "brandName": "Cool Brand Name",
  "deviceName": "A Cooler Device Name",
  "deviceDescription": "Device Description",
  "targetCountry": [
    "ID",
    "MY"
  ],
  "deviceConfiguration": {
    "minValue": 16,
    "maxValue": 30,
    "defaultValue": 25
  }
}
```

Validations:\
`brandName`: required, max length 100\
`deviceName`: required, max length 150\
`targetCountry`: ISO 3166-1 alpha-2 (e.g., "ID", "SG")\
`deviceConfiguration`: required\
`deviceConfiguration.defaultValue`: must be between minValue and maxValue\
Example response:\
`201` Device Created

```json
{
  "success": true,
  "message": "Device successfully created"
}
```

`400` Validation Error

```json
{
  "success": false,
  "message": "Validation failed when creating device",
  "errors": [
    "deviceConfiguration.defaultValue: defaultValue must be between minValue and maxValue",
    "brandName: size must be between 0 and 100",
    "deviceName: size must be between 0 and 150"
  ]
}
```

* `GET`: get all devices owned.
  Example response:\
  `200` Success get device

```json
{
  "success": true,
  "message": "devices found",
  "data": [
    {
      "brandName": "Cool Brand Name",
      "deviceName": "A Cooler Device Name",
      "description": "Device Description"
    },
    {
      "brandName": "Cool Brand Name",
      "deviceName": "An even Cooler Device Name",
      "description": "Device Description 2"
    },
    {
      "brandName": "Another Brand Name",
      "deviceName": "Another Device Name",
      "description": "Device Description 3"
    }
  ]
}
```

* `PUT`: update a device owned. Example request:

```json
{
  "id": 1,
  "brandName": "Cool Brand Name",
  "deviceName": "The Coolest Device Name",
  "deviceDescription": "Device Description",
  "targetCountry": [
    "ID",
    "MY",
    "SG"
  ],
  "deviceConfiguration": {
    "minValue": 16,
    "maxValue": 32,
    "defaultValue": 25
  }
}
```

Validations:\
`id`: required, has to exist, not deleted, and owned by vendor\
`brandName`: required, max length 100\
`deviceName`: required, max length 150\
`targetCountry`: ISO 3166-1 alpha-2 (e.g., "ID", "SG")\
`deviceConfiguration`: required\
`deviceConfiguration.defaultValue`: must be between minValue and maxValue\
Example response:\
`200` Device Updated

```json
{
  "success": true,
  "message": "Device successfully Updated"
}
```

`400` Validation Error

```json
{
  "success": false,
  "message": "Validation failed when updating device",
  "errors": [
    "deviceConfiguration.defaultValue: defaultValue must be between minValue and maxValue",
    "brandName: size must be between 0 and 100",
    "deviceName: size must be between 0 and 150"
  ]
}
```

`422` Invalid device to be updated. Either device does not exist, deleted, or belongs to another vendor

```json
{
  "success": false,
  "message": "Failed to update device",
  "errors": [
    "Device to be updated not found."
  ]
}
```

* `DELETE`: delete device owned. Example request:

```json
{
  "deviceId": 1
}
```

Validations:\
`deviceId`: required, has to exist, not deleted, and owned by vendor\
Example response:\
`200`: device successfully deleted

```json
{
  "success": true,
  "message": "Device successfully deleted"
}
```

`422`: Invalid device to be deleted. Either device does not exist, deleted, or belongs to another vendor

```json
{
  "success": false,
  "message": "Failed to delete device",
  "errors": [
    "Device to be deleted not found."
  ]
}
```

`400`: Validation Error

```json
{
  "success": false,
  "message": "Validation failed when deleting device",
  "errors": [
    "deviceId: must not be null"
  ]
}
```

#### /api/vendor/device-information

* `GET`: get all device information owned.
  Example response:\
  `200` Success get device information

```json
{
  "success": true,
  "message": "devices found",
  "data": [
    {
      "id": 1,
      "brandName": "Cool Brand Name",
      "deviceName": "A Cooler Device Name",
      "description": "Device Description",
      "targetCountry": [
        {
          "code": "FR",
          "countryName": "France"
        },
        {
          "code": "ID",
          "countryName": "Indonesia"
        }
      ],
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      }
    },
    {
      "id": 1,
      "brandName": "Cool Brand Name",
      "deviceName": "A Cooler Device Name",
      "description": "Device Description",
      "targetCountry": [
        {
          "code": "FR",
          "countryName": "France"
        },
        {
          "code": "ID",
          "countryName": "Indonesia"
        }
      ],
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      }
    },
    {
      "id": 3,
      "brandName": "Another Brand Name",
      "deviceName": "Another Device Name",
      "description": "Device Description 3",
      "targetCountry": [
        {
          "code": "FR",
          "countryName": "France"
        },
        {
          "code": "ID",
          "countryName": "Indonesia"
        }
      ],
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      }
    }
  ]
}
```

### User Endpoints

Required role to access these endpoints is `user`. Users without the required role will be given the response below
`403`

```json
{
  "success": false,
  "message": "Forbidden"
}
```

#### /api/users/available-devices

* `GET`: get available devices available based on user's country\
  Example Response:

```json
{
  "success": true,
  "message": "Available devices found",
  "data": [
    {
      "id": 1,
      "brandName": "Cool Brand Name",
      "deviceName": "A Cooler Device Name",
      "description": "Device Description",
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      }
    },
    {
      "id": 2,
      "brandName": "Cool Brand Name",
      "deviceName": "An even Cooler Device Name",
      "description": "Device Description 2",
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      }
    },
    {
      "id": 3,
      "brandName": "Another Brand Name",
      "deviceName": "Another Device Name",
      "description": "Device Description 3",
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      }
    }
  ]
}
```

#### /api/users/devices

* `POST`: register a device to user. Example request:

```json
{
  "deviceId": 1
}
```

Validations:\
`deviceId`: required, required, has to exist, not deleted, and available in user's country\
Example response:\
`201` User Device Successfully registered

```json
{
  "success": true,
  "message": "Device successfully registered"
}
```

`400` Validation Error

```json
{
  "success": false,
  "message": "Validation failed when registering device",
  "errors": [
    "deviceId: must not be null"
  ]
}
```

`422` Invalid device to be registered. Either device does not exist, deleted, or unavailable on user's country

```json
{
  "success": false,
  "message": "Failed to register device",
  "errors": [
    "Device is not available in user's country or does not exist"
  ]
}
```

* `GET`: get all devices registered.
  Example response:\
  `200` Success get registered device

```json
{
  "success": true,
  "message": "Registered devices found",
  "data": [
    {
      "userDeviceId": 1,
      "deviceId": 1,
      "brandName": "Cool Brand Name",
      "deviceName": "A Cooler Device Name",
      "description": "Device Description",
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      },
      "value": 25
    },
    {
      "userDeviceId": 2,
      "deviceId": 1,
      "brandName": "Cool Brand Name",
      "deviceName": "A Cooler Device Name",
      "description": "Device Description",
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      },
      "value": 20
    },
    {
      "userDeviceId": 3,
      "deviceId": 3,
      "brandName": "Another Brand Name",
      "deviceName": "Another Device Name",
      "description": "Device Description 3",
      "deviceConfiguration": {
        "minValue": 16,
        "maxValue": 30,
        "defaultValue": 25
      },
      "value": 21
    }
  ]
}
```

* `PUT`: update a device owned. Example request:

```json
{
  "userDeviceId": 1,
  "value": 10
}
```

Validations:\
`userDeviceId`: required, has to exist and registered to user\
`value`: required, has to be between device's minValue and maxValue\
Example response:\
`200` Device Updated

```json
{
  "success": true,
  "message": "Device value successfully updated"
}
```

`400` Validation Error

```json
{
  "success": false,
  "message": "Failed to update device value",
  "errors": [
    "Value must be between 0 and 10"
  ]
}
```

`422` Invalid device to be updated. Either device does not exist or not registered by user

```json
{
  "success": false,
  "message": "Failed to update device value",
  "errors": [
    "Device not found or not owned by user."
  ]
}
```

* `DELETE`: unregister device. Only able to unregister an already registered device to user. Example request:

```json
{
  "userDeviceId": 1
}
```

Validations:\
`userDeviceId`: required, has to exist and registered to user\
Example response:\
`200`: device successfully deleted

```json
{
  "success": true,
  "message": "Device successfully unregistered"
}
```

`422`: Invalid device to be unregistered. Either user device does not exist or not registered by user

```json
{
  "success": false,
  "message": "Failed to unregister device",
  "errors": [
    "Device to be unregistered not found."
  ]
}
```

`400`: Validation Error

```json
{
  "success": false,
  "message": "Validation failed when unregistering device",
  "errors": [
    "userDeviceId: must not be null"
  ]
}
```

### Admin Endpoints

Required role to access these endpoints is `admin`. Users without the required role will be given the response below
`403`

```json
{
  "success": false,
  "message": "Forbidden"
}
```

#### /api/admin/devices

* `GET`: get all devices with user count.
  Example response: \
  `200` Success get devices with user count

```json
{
  "success": true,
  "message": "Devices found",
  "data": [
    {
      "deviceName": "A Cooler Device Name",
      "registeredUsers": 1
    },
    {
      "deviceName": "Another Device Name",
      "registeredUsers": 2
    }
  ]
}
```

#### /api/admin/users

* `GET`: get all users with device registered count.
  Example response:\
  `200` Success get users with device registered count

```json
{
  "success": true,
  "message": "User device count found",
  "data": [
    {
      "username": "exampleuser",
      "fullName": "John Doe",
      "registeredDeviceCount": 2
    },
    {
      "username": "anotheruser",
      "fullName": "Jane Doe",
      "registeredDeviceCount": 1
    },
    {
      "username": "andanotherone",
      "fullName": "Troy Doe",
      "registeredDeviceCount": 5
    }
  ]
}
```

#### /api/admin/user-information

* `GET`: get all user's information.
  Example response:\
  `200` Success get user's information

```json
{
  "success": true,
  "message": "User information found",
  "data": [
    {
      "username": "exampleuser",
      "fullName": "John Doe",
      "dob": "1990-01-01",
      "address": "123 Example Street, City",
      "country": "Indonesia"
    },
    {
      "username": "anotheruser",
      "fullName": "Jane Doe",
      "dob": "1990-01-01",
      "address": "123 Example Street, City",
      "country": "France"
    },
    {
      "username": "andanotherone",
      "fullName": "Troy Doe",
      "dob": "1999-01-01",
      "address": "123 Example Street, City",
      "country": "Malaysia"
    }
  ]
}
```

## Testing

You can test the API above using the provided [Postman Collection](Smartthings.postman_collection.json)