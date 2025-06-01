CREATE TABLE devices (
    id INT IDENTITY(1,1) PRIMARY KEY,
    brand_name VARCHAR(100) NOT NULL,
    device_name VARCHAR(150) NOT NULL,
    device_description TEXT,
    min_value INT NOT NULL,
    max_value INT NOT NULL,
    default_value INT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    deleted_at DATETIME
);

CREATE TABLE countries (
    code CHAR(2) PRIMARY KEY,
    country_name VARCHAR(100) NOT NULL
);


CREATE TABLE users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT SYSDATETIME()
);

GO

CREATE TABLE smartthings_user_profiles (
    user_id INT PRIMARY KEY
    full_name VARCHAR(100),
    dob DATE,
    user_address TEXT,
    country_code CHAR(2),
    FOREIGN KEY (country_code) REFERENCES countries(code),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

GO

CREATE TABLE user_devices (
    id INT IDENTITY(1,1) PRIMARY KEY,
    current_value INT,
    user_id INT,
    device_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (device_id) REFERENCES devices(id)
);

CREATE TABLE device_target_countries (
    country_code CHAR(2),
    device_id INT,
    PRIMARY KEY (device_id, country_code),
    FOREIGN KEY (country_code) REFERENCES countries(code),
    FOREIGN KEY (device_id) REFERENCES devices(id)
);
