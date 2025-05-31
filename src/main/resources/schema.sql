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

CREATE TABLE user_devices (
    id INT IDENTITY(1,1) PRIMARY KEY,
    current_value INT,
    user_id INT,
    device_id INT,
    FOREIGN KEY (user_id) REFERENCES smartthings_users(id),
    FOREIGN KEY (device_id) REFERENCES devices(id)
);

CREATE TABLE smartthings_users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    user_name VARCHAR(100),
    dob DATE,
    address TEXT,
    country_code CHAR(2),
    FOREIGN KEY (country_code) REFERENCES countries(code)
);

CREATE TABLE countries (
    code CHAR(2) PRIMARY KEY,
    country_name
);

CREATE TABLE device_target_countries (
    country_code CHAR(2),
    device_id INT,
    PRIMARY KEY (device_id, country_code),
    FOREIGN KEY (country_code) REFERENCES countries(code),
    FOREIGN KEY (device_id) REFERENCES devices(id)
);