package cn.aysst.www.flowerfish.wheelview;
import java.util.Date;


public class TimeRange {

    private String start_time;
    private String end_time;

    public Date getStart_time() {
        return Common.dateTimeFromStr(start_time);
    }

    public void setStart_time(Date start_time) {
        this.start_time = Common.dateTimeToStr(start_time);
    }

    public Date getEnd_time() {
        return Common.dateTimeFromStr(end_time);
    }

    public void setEnd_time(Date end_time) {
        this.end_time = Common.dateTimeToStr(end_time);
    }
}
