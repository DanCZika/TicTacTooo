package com.example.dani.tictactooo;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Dani on 2017. 04. 02..
 */

public class GameData {

    private int Available;
    protected int PlayerNO;
    private String P1;
    private String P2;
    private int[] Map;  //0 üres 1 x 2 O
    private  String ErrorStr;
    private String serverurl = "http://midas.ktk.bme.hu/poti/";
    private int Refresh;

    /*
    map:

    [0  1   2
     3  4   5
     6  7   8]

     */

    public int getAvailable() {
        return Available;
    }

    public void setAvailable(int available) {
        Available = available;
    }

    public String getP1() {
        return P1;
    }

    public void setP1(String p1) {
        P1 = p1;
    }

    public String getP2() {
        return P2;
    }

    public void setP2(String p2) {
        P2 = p2;
    }

    public int[] getMap() {
        return Map;
    }

    public void setMap(int[] map) {
        Map = map;
    }

    public  void setCell(int cell, int val) { Map[cell] = val; }

    public String getErrorStr() { return ErrorStr; }

    public void setErrorStr(String errorStr) { ErrorStr = errorStr; }

    public int getPlayerNO() {
        return PlayerNO;
    }

    public void setPlayerNO(int available) {
        if (available == 0 ) { PlayerNO = 1; }
        else if (available == 1 ) { PlayerNO = 2;}
        else {PlayerNO = 0; }
    }

    public int getRefresh() {
        return Refresh;
    }

    public void setRefresh(int refresh) {
        Refresh = refresh;
    }

    public void setPlayerNO_d(int playerNO) {
        PlayerNO = playerNO;
    }

    public GameData datafromurl(String str)
    {
        GameData gd = new GameData();

        String[] parts = str.split(";");

        gd.setAvailable(Integer.parseInt(parts[0]));
        gd.setP1(parts[1]);
        gd.setP2(parts[2]);
        int[] asd = new int[9];
        for (int i = 0; i < 9; i++)
        {
            asd[i] = Integer.parseInt(parts[i+3]);
        }
        gd.setMap(asd);
        gd.setRefresh(Integer.parseInt(parts[12]));

        return gd;
    }

    public int hasSomeoneWon() {
        int winner = 0;
        for (int i = 0; i < 3; i++) {
            {
                if (Map[3*i] != 0 && Map[3*i] == Map[3*i+1] && Map[3*i] == Map[3*i+2]) {
                    winner = Map[3*i];
                } //sorok ellenorzese
                if (Map[i] != 0 && Map[i] == Map[i+3] && Map[i] == Map[i+6]) {
                    winner = Map[i];
                } //oszlopok ellenorzes
            }
        }
        if ((Map[4] != 0 && (Map[4] == Map[0] && Map[4] == Map[8]) ||
                (Map[4] == Map[2] && Map[4] == Map[6]))) {
            winner = Map[4];
        } //atlok ellenorzese
        return winner;
    }

    public boolean isGameEnd() {
        boolean ended = true;
        for (int i = 0; i < 9; i++) {
            if (Map[i] == 0) {
                ended = false;
            }
        } //ha nincs tobb ures mezo, akkor igaz
        return  ended;
    }

}
