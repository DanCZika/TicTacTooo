package com.example.dani.tictactooo;

import android.net.http.RequestQueue;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.util.*;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    EditText etP1;
    TextView tvP2;
    TextView tverror;
    TextView tvplayerid;
    Button startbtn;
    Button refreshbtn;
    Button restartbtn;
    Button btnMap[] = new  Button[9];

    GameData gameData = new GameData();

    String serverurl = "http://midas.ktk.bme.hu/poti/";

    int playerNO =0;

    int refresh = 0;

    int upstat;

    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startbtn = (Button)findViewById(R.id.startbtn);
        restartbtn = (Button)findViewById(R.id.restartbtn);
        tvP2 = (TextView)findViewById(R.id.p2id);
        tverror = (TextView)findViewById(R.id.error1);
        tvplayerid =(TextView)findViewById(R.id.playerid);
        etP1 = (EditText) findViewById(R.id.p1id);
        refreshbtn = (Button)findViewById(R.id.refreshbtn);
        btnMap[0] = (Button) findViewById(R.id.btn0);
        btnMap[1] = (Button) findViewById(R.id.btn1);
        btnMap[2] = (Button) findViewById(R.id.btn2);
        btnMap[3] = (Button) findViewById(R.id.btn3);
        btnMap[4] = (Button) findViewById(R.id.btn4);
        btnMap[5] = (Button) findViewById(R.id.btn5);
        btnMap[6] = (Button) findViewById(R.id.btn6);
        btnMap[7] = (Button) findViewById(R.id.btn7);
        btnMap[8] = (Button) findViewById(R.id.btn8);

        refreshbtn.setVisibility(View.GONE);

        tvP2.setText("");
        tvplayerid.setText("");
        tverror.setText("");
        for (int i = 0;i<9;i++)
        {
            btnMap[i].setText("");
        }

        updateData();

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawMap(gameData);
                gameData.setPlayerNO(gameData.getAvailable());
                //tverror.setText("NO"+Integer.toString(gameData.getPlayerNO())+ "avala" + Integer.toString(gameData.getAvailable()));

                if (gameData.getAvailable()== 0)
                {
                    tvplayerid.setText(R.string.youX);
                    gameData.setAvailable(1);
                    gameData.setP1(etP1.getText().toString());
                    playerNO = gameData.getPlayerNO();
                    uploadData();
                    tvP2.setText(R.string.wait2);

                    refresh = 1;

                    for (int i =0; i <9;i++) { btnMap[i].setClickable(false); }


                }

                else if (gameData.getAvailable() == 1 )
                {
                    tvplayerid.setText(R.string.youO);
                    gameData.setAvailable(2);
                    gameData.setP2(etP1.getText().toString());
                    playerNO = gameData.getPlayerNO();

                    tvP2.setText(gameData.getP1());
                    uploadData();
                    tverror.setText(R.string.wait1);

                    refresh = 1;

                    for (int i =0; i <9;i++) { btnMap[i].setClickable(false); }

                }

                else {
                    tvplayerid.setText(R.string.serverfull);
                }

                startbtn.setClickable(false);
                etP1.setEnabled(false);

            }
        });

        restartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameData.getRefresh()==0)
                {
                    gameData.setRefresh(gameData.getPlayerNO());
                } else if (gameData.getRefresh()!=gameData.getPlayerNO())
                {
                    gameData.setRefresh(0);
                    tverror.setText("");
                }
                restarted();
            }
        });

        for (int i =0 ; i < 9;i++)
        {
            final int finalI = i;
            btnMap[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //tverror.setText("you hit " + Integer.toString(finalI));

                    if (gameData.getPlayerNO() == 1 && gameData.getAvailable()== 2 )
                    {
                        gameData.setCell(finalI, gameData.getPlayerNO() );
                        drawMap(gameData);
                        gameData.setAvailable(3);
                        tverror.setText(R.string.wait2);
                        uploadData();

                        for (int i =0; i <9;i++) { btnMap[i].setClickable(false); }

                    }

                    if (gameData.getPlayerNO() == 2 && gameData.getAvailable()== 3 )
                    {
                        gameData.setCell(finalI, gameData.getPlayerNO() );
                        drawMap(gameData);
                        gameData.setAvailable(2);
                        tverror.setText(R.string.wait1);
                        uploadData();



                        for (int i =0; i <9;i++) { btnMap[i].setClickable(false); }
                    }





                }
            });
        }

        /*refreshbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();

                if (gameData.getPlayerNO() == 1 && gameData.getAvailable()== 2)
                {
                    tvP2.setText(gameData.getP2());
                    drawMap(gameData);
                    tverror.setText("your turn P1");

                }

                if (gameData.getPlayerNO() == 2 && gameData.getAvailable()== 3)
                {
                    drawMap(gameData);
                    tverror.setText("your turn P2");
                }

            }
        });*/


      /*  new CountDownTimer(Integer.MAX_VALUE, 500)
        {
            public void onTick(long millisUntilFinished)
            {
                // do something every 5 seconds...
                updateData();
                //draw(gameData);

                if (gameData.getPlayerNO() == 1 && gameData.getAvailable()== 2)
                {
                    tvP2.setText(gameData.getP2());
                    drawMap(gameData);
                    tverror.setText("your turn P1");

                }

                if (gameData.getPlayerNO() == 2 && gameData.getAvailable()== 3)
                {
                    drawMap(gameData);
                    tverror.setText("your turn P2");
                }

            }

            public void onFinish()
            {
                // finish off when we're all dead !
            }
        }.start();*/


        Timer t = new Timer();
        t.scheduleAtFixedRate( new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() //run on ui thread
                {
                    public void run() {
                        toast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
                        toast.cancel();
                        try {
                            int tempPlayerNO = gameData.getPlayerNO();
                            updateData();
                            gameData.setPlayerNO_d(tempPlayerNO);
                            if (tvP2.getText().equals("")) {for (int i = 0; i < 9; i++) {btnMap[i].setClickable(false);}}
                            if (gameData.getRefresh()!=0 && gameData.getRefresh()!=gameData.getPlayerNO())
                            {
                                tverror.setText(R.string.restartreq);
                                tvplayerid.setText("");
                                for (int i = 0; i < 9; i++) {
                                    btnMap[i].setClickable(false);
                                }
                            } else if (gameData.getRefresh()!=0 && gameData.getRefresh()==gameData.getPlayerNO())
                            {
                                tverror.setText(R.string.wait4res);
                                startbtn.setClickable(false);
                            } else
                            {
                                if (gameData.getPlayerNO() == 1 && gameData.getAvailable() == 2) {
                                    tvP2.setText(gameData.getP2());
                                    drawMap(gameData);
                                    tverror.setText(R.string.turnP1);

                                }

                                if (gameData.getPlayerNO() == 2 && gameData.getAvailable() == 3) {
                                    drawMap(gameData);
                                    tverror.setText(R.string.turnP2);
                                }
                                if (gameData.hasSomeoneWon() != 0 || gameData.isGameEnd()) {
                                    endGame(gameData.hasSomeoneWon());
                                }
                                if (tverror.getText().equals(getString(R.string.wait4res)) || tverror.getText().equals(getString(R.string.restartreq)))
                                {
                                    tverror.setText(R.string.ready2start);
                                    startbtn.setClickable(true);
                                }
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 0, 500 );

    }


    public void restarted() {
        tvP2.setText("");
        tvplayerid.setText("");
        gameData.setAvailable(0);
        int[] map = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gameData.setMap(map);
        try {
            uploadData();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        refresh = 1;
        for (int i = 0; i < 9; i++) {
            btnMap[i].setClickable(true);
            btnMap[i].setText("");
        }

        startbtn.setClickable(true);
        etP1.setEnabled(true);
    }

    public GameData datafromurl(String str)
    {
        GameData gd = new GameData();

        String[] parts = str.split(";");

        gd.setAvailable(Integer.parseInt(parts[0]));
        gd.setP1(parts[1]);
        gd.setP2(parts[2]);
        gd.setPlayerNO_d(playerNO);
        int[] asd = new int[9];
        for (int i = 0; i < 9; i++)
        {
            asd[i] = Integer.parseInt(parts[i+3]);
        }
        gd.setMap(asd);
        gd.setRefresh(Integer.parseInt(parts[12]));


        return gd;
    }

    public void drawMap (GameData gd)
    {

        for (int i = 0; i<9;i++)
        {
            btnMap[i].setText( XorO(gd.getMap()[i]) );
            if(! "".equals(XorO(gd.getMap()[i])))
            {
                btnMap[i].setClickable(false);
            }

            else { btnMap[i].setClickable(true); }

        }


    }

    public void draw (GameData gd)
    {

        for (int i = 0; i<9;i++)
        {
            btnMap[i].setText( XorO(gd.getMap()[i]) );

        }


    }

    public String XorO (int e)
    {
        String val= "";
        if (e == 1) val = "X";
        if (e == 2) val = "O";
        return val;
    }

    public void uploadData()
    {
        upstat = 0;

        final com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, generateURL(gameData),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.endsWith("OK") )
                        {
                            upstat =1;
                            //tverror.setText("upload sucsess");
                        }

                        else upstat=0;

                        requestQueue.stop();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                upstat = 0;
                tverror.setText(R.string.upfail);
                error.printStackTrace();
                //downloaded ="";
                requestQueue.stop();

            }
        });

        requestQueue.add(stringRequest);


    }

    public String generateURL(GameData gd)
    {
        String generated;
        String MAP ="";
        for(int i = 0; i <9;i++)
        {

            MAP = MAP+ Integer.toHexString(gd.getMap()[i])+ ";" ;
        }

        if (gameData.getAvailable() == 0) {
            generated = getString(R.string.url4csv) + gameData.getAvailable() + ";" + gd.getP1() + ";waitingforplayer2;" + MAP + gameData.getRefresh() + ";";
        }

        else
        {
            generated = getString(R.string.url4csv) + gameData.getAvailable() + ";" + gd.getP1() + ";" + gd.getP2() + ";" + MAP + gameData.getRefresh() + ";";
        }


        return generated;
    }

    public void updateData()
    {

        final com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //tverror.setText(response);
                        gameData  = datafromurl(response);
                        //tverror.setText(gameData.getAvailable());
                        requestQueue.stop();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                tverror.setText(R.string.fail2con);
                error.printStackTrace();
                requestQueue.stop();

            }
        });
        requestQueue.add(stringRequest);

    }

    public void endGame(int winner) {
        String endText = getString(R.string.game_over);
        if (winner == 1) {
            endText = getString(R.string.x_won);
        }
        if (winner == 2) {
            endText = getString(R.string.o_won);
        }

        toast = Toast.makeText(MainActivity.this, endText, Toast.LENGTH_SHORT);
        toast.show();
    }

}

