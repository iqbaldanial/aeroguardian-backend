package com.plancana.aeroguardian.base.enums;


import lombok.Getter;

@Getter
public enum AqiLevel {
    GOOD(0,50,"Air quality is considered satisfactory, and air pollution poses little or no risk","None"),
    MODERATE(51,100,"Air quality is acceptable; however, for some pollutants there may be a moderate health concern for a very small number of people who are unusually sensitive to air pollution.","Active children and adults, and people with respiratory disease, such as asthma, should limit prolonged outdoor exertion."),
    UNHEALTHY_SENSITIVE(101,150,"Members of sensitive groups may experience health effects. The general public is not likely to be affected.","Active children and adults, and people with respiratory disease, such as asthma, should limit prolonged outdoor exertion."),
    UNHEALTHY(151,200,"Everyone may begin to experience health effects; members of sensitive groups may experience more serious health effects","Active children and adults, and people with respiratory disease, such as asthma, should avoid prolonged outdoor exertion; everyone else, especially children, should limit prolonged outdoor exertion"),
    VERY_UNHEALTHY(201,300,"Health warnings of emergency conditions. The entire population is more likely to be affected.","Active children and adults, and people with respiratory disease, such as asthma, should avoid all outdoor exertion; everyone else, especially children, should limit outdoor exertion."),
    HAZARDOUS(301,999,"Health alert: everyone may experience more serious health effect","Everyone should avoid all outdoor exertion");

    private final int min;
    private final int max;
    private final String description;
    private final String caution;

    AqiLevel(int min, int max, String description, String caution) {
        this.min = min;
        this.max = max;
        this.description = description;
        this.caution = caution;
    }

    public static AqiLevel fromAQI(double aqi){
        for(AqiLevel level : values()){
            if(aqi >=level.min && aqi <=level.max){
                return level;
            }
        }
        return HAZARDOUS;
    }

}
