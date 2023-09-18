package model;

import java.io.Serializable;
import java.sql.Time;

/**
 *
 * @author admin
 */
public class User implements Serializable {

    private String name;
    private String username;
    private String password;
    private String rePass;
    private double point;
    private String status;
    private Time time;
    private String type;
    private int winGames;
    private String averageTime;

    public User() {
        super();
    }

    public User(String name, double point, String status) {
        this.name = name;
        this.point = point;
        this.status = status;
    }

    public User(String name, String username, String password, String rePass, double point, String status, Time time, int winGames) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.rePass = rePass;
        this.point = point;
        this.status = status;
        this.time = time;
        this.winGames = winGames;
    }

    public Time getTime() {
        return this.time;
    }

    public int getWinGames() {
        return winGames;
    }

    public void setWinGames(int winGames) {
        this.winGames = winGames;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePass() {
        return rePass;
    }

    public void setRePass(String rePass) {
        this.rePass = rePass;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(String averageTime) {
        this.averageTime = averageTime;
    }

    @Override
    public String toString() {
        return name + " - " + point + " - " + status;
    }

    public String toStringg() {
        return name + " " + point + " " + averageTime;
    }
}
