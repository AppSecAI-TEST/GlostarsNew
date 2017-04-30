package com.golstars.www.glostars;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Created by edson on 16/03/17.
 */

public class Timestamp {

    public static String getInterval(LocalDateTime startTime){

        LocalDateTime endTime = new LocalDateTime();
        Period period = new Period(startTime, endTime);


        //a year or more
        if(period.getYears() > 1){

            return period.getYears() + " years ago";

        } else if(period.getYears() == 1){

            return "year ago";

        } else if(period.getYears() < 1){

            //less than a year
            if(period.getMonths() > 1){

                return period.getMonths() + " months ago";

            } else if(period.getMonths() == 1){

                return "month ago";

            } else if(period.getMonths() < 1){

                // less than a month
                if (period.getWeeks() > 1){

                    return period.getWeeks() + " weeeks ago";

                } else if(period.getWeeks() == 1){

                    return "a week ago";

                } else if(period.getWeeks() < 1){

                    // less than 7 days
                    if(period.getDays() > 1){

                        return period.getDays() + " days ago";

                    } else if(period.getDays() == 1){

                        return "Yesterday";

                    } else if(period.getDays() < 1){

                        // less than 24 hours
                        if(period.getHours() > 1){

                            return period.getHours() + " hours ago";

                        } else if(period.getHours() == 1){

                            return "hour ago";

                        } else if(period.getHours() < 1){

                            // less than 60 minutes
                            if(period.getMinutes() > 1){

                                return period.getMinutes() + " minutes ago";

                            } else if(period.getMinutes() == 1){

                                return " a minute ago";

                            } else if (period.getMinutes() < 0){

                                return "just now";

                            }

                        }
                    }


                }

            }


        }


        return startTime.toString();



    }
    public static String getOwnZoneTime(String s){
        String[] date=s.split("\\.");
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTime dateTime= DateTime.parse(date[0], formatter).withZone(DateTimeZone.UTC);
        return dateTime.toString("yyyy-MM-dd HH:mm:ss");
    }

    public static LocalDateTime getOwnZoneDateTime(String s){
        DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(s);
        return dt.toLocalDateTime();
    }

}
