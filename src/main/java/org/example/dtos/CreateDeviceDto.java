package org.example.dtos;

import java.util.List;

public class CreateDeviceDto {
    private String brandName;
    private String deviceName;
    private String deviceDescription;
    private List<String> targetCountry;
    private DeviceConfigurationDto deviceConfiguration;
    private Integer value;

    public CreateDeviceDto(String brandName, String deviceName, String deviceDescription, List<String> targetCountry, DeviceConfigurationDto deviceConfiguration, Integer value) {
        this.brandName = brandName;
        this.deviceName = deviceName;
        this.deviceDescription = deviceDescription;
        this.targetCountry = targetCountry;
        this.deviceConfiguration = deviceConfiguration;
        this.value = value;
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

    public List<String> getTargetCountry() {
        return targetCountry;
    }

    public DeviceConfigurationDto getDeviceConfiguration() {
        return deviceConfiguration;
    }

    public Integer getValue() {
        return value;
    }

    public static class DeviceConfigurationDto {
        private Integer minValue;
        private Integer maxValue;
        private Integer defaultValue;

        public DeviceConfigurationDto(Integer minValue, Integer maxValue, Integer defaultValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.defaultValue = defaultValue;
        }

        public Integer getMinValue() {
            return minValue;
        }

        public Integer getMaxValue() {
            return maxValue;
        }

        public Integer getDefaultValue() {
            return defaultValue;
        }
    }
}



















