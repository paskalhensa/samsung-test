package org.example.dtos;

import java.util.List;

public class DeviceInformationDto {
    private Integer id;
    private String brandName;
    private String deviceName;
    private String deviceDescription;
    private List<CountryDto> targetCountry;
    private CreateDeviceDto.DeviceConfigurationDto deviceConfiguration;

    public DeviceInformationDto(Integer id, String brandName, String deviceName, String deviceDescription,
                                List<CountryDto> targetCountry,
                                CreateDeviceDto.DeviceConfigurationDto deviceConfiguration) {
        this.id = id;
        this.brandName = brandName;
        this.deviceName = deviceName;
        this.deviceDescription = deviceDescription;
        this.targetCountry = targetCountry;
        this.deviceConfiguration = deviceConfiguration;
    }

    public DeviceInformationDto() {
    }

    public Integer getId() {
        return id;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public List<CountryDto> getTargetCountry() {
        return targetCountry;
    }

    public CreateDeviceDto.DeviceConfigurationDto getDeviceConfiguration() {
        return deviceConfiguration;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    public void setTargetCountry(List<CountryDto> targetCountry) {
        this.targetCountry = targetCountry;
    }

    public void setDeviceConfiguration(CreateDeviceDto.DeviceConfigurationDto deviceConfiguration) {
        this.deviceConfiguration = deviceConfiguration;
    }

    public static class CountryDto {
        private String code;
        private String countryName;

        public CountryDto() {
        }

        public CountryDto(String code, String countryName) {
            this.code = code;
            this.countryName = countryName;
        }

        public String getCode() {
            return code;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

    }
}
