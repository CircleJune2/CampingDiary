/**
 * MainActivity.java
 * AppIronDemo
 *
 * Created on 2019. 12. 6.
 * Copyright (c) 2019 Secucen. All rights reserved.
 */
package com.secucen.appiron.campingdiary;

import com.barun.appiron.android.AppIron;
import com.barun.appiron.android.AppIronException;
import com.barun.appiron.android.AppIronResult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    private AppIron appIron;
    private static final String TAG = "MainActivity";

    private EditText editUrl;
    private Button visitListBtn;
    private Button authAppBtn;

    private int count = 0;
  /*---------------------------------------------------------------------------*
   * 앱아이언 검증서버 url
   *---------------------------------------------------------------------------*/
  //private static final String APPIRON_AUTHCHECK_URL = "http://192.168.54.104:48080/appiron-inspect/authCheck.call";
  String APPIRON_AUTHCHECK_URL = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);



    //text view 보여주지 않음
      editUrl = (EditText) findViewById(R.id.editUrl);
      editUrl.setVisibility(View.INVISIBLE);
      authAppBtn = (Button) findViewById(R.id.authAppBtn);

      //버튼이벤트 itemListBtn
    findViewById(R.id.itemListBtn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

          if (count %10 == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("업데이트 예정...").setMessage("준비중인 메뉴입니다.");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
          }

          else if (count %10 == 8){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("카운트9").setMessage("마지막 한번 더 터치하면 검증!");
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            //text view 보여줌
            editUrl.setVisibility(View.VISIBLE);
            authAppBtn.setVisibility(View.VISIBLE);
            }
          else if (count %10 == 9){
            //text로 입력받은 url 담아
            editUrl = (EditText) findViewById(R.id.editUrl);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            //builder.setTitle("카운트10").setMessage("검증을 시작합니다.");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            authApp_old();

          }

            count ++;
        }
    });

    initViewAndAction();


  }
  private void initViewAndAction() {
    visitListBtn = (Button) findViewById(R.id.visitListBtn);

    visitListBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        VisitActivity(editUrl.getText().toString());
      }
    });
  }

  private void VisitActivity(String appironAuthUrl) {

    Intent intent = new Intent(this, VisitActivity.class);
    intent.putExtra("appironAuthUrl", appironAuthUrl);
    startActivity(intent);
  }

  public void authApp_old(){

    APPIRON_AUTHCHECK_URL = editUrl.getText().toString();
    //text박스에 빈값일 경우
    if(APPIRON_AUTHCHECK_URL.getBytes().length <= 0){
      Toast.makeText(getApplicationContext(), "텍스트를 입력하세요.", Toast.LENGTH_LONG).show();
    }
    else{
      Log.d(TAG, "APPIRON_AUTHCHECK_URL");
      Log.d(TAG, APPIRON_AUTHCHECK_URL);

      appIron = AppIron.getInstance(this);
      String appironResult_old = appIron.authApp(APPIRON_AUTHCHECK_URL);


      //Toast myToast = Toast.makeText(this.getApplicationContext(), "", Toast.LENGTH_SHORT);
      Toast.makeText(getApplicationContext(), appironResult_old, Toast.LENGTH_LONG).show();
    }

  }


  /*---------------------------------------------------------------------------*
   * 앱아이언 1차 검증 호출
   *---------------------------------------------------------------------------*/
  public void authApp(View view) {
	/*---------------------------------------------------------------------------*
	 * 신규 api 호출
     *---------------------------------------------------------------------------*/
    AppIronTask aAppIronTask = new AppIronTask();
    aAppIronTask.execute();

    /*---------------------------------------------------------------------------*
	 * legacy api (deprecated) 호출
	 *---------------------------------------------------------------------------*/
//    authAppLegacy();
  }

  private void authAppLegacy() {
    AppIron aAppIron = AppIron.getInstance(MainActivity.this);
	String aResultCode = aAppIron.authApp(APPIRON_AUTHCHECK_URL);

	AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

	if (aResultCode.equals("0000")) {
	  alert.setTitle("[무결성 검증 성공]");
	  alert.setMessage(String.format("세션아이디 [%s]\n토큰 [%s]",
	              aAppIron.getSessionId(),
	              aAppIron.getToken()));
	} else {
	  /*---------------------------------------------------------------------------*
	   * 에러 출력
	   *---------------------------------------------------------------------------*/
	  alert.setTitle("[무결성 검증 실패]");
	  alert.setMessage(aResultCode);
	}

	alert.setCancelable(false);
	alert.setPositiveButton("close", null);
	alert.show();
  }

  class AppIronTask extends AsyncTask<Void, Void, AppIronResult> {
    private ProgressDialog mProgressDialog;
    private AppIronException mAppIronException;

    @Override
    protected void onPreExecute() {

        /*---------------------------------------------------------------------------*
         * 진행 다이얼로그 보이기
         *---------------------------------------------------------------------------*/
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("무결성 검증 중입니다.\n잠시만 기다려 주세요...!");
        mProgressDialog.show();


    }

    @Override
    protected AppIronResult doInBackground(Void... voids) {

        APPIRON_AUTHCHECK_URL = editUrl.getText().toString();
        Log.d(TAG, "APPIRON_AUTHCHECK_URL");
        Log.d(TAG, APPIRON_AUTHCHECK_URL);

      if(APPIRON_AUTHCHECK_URL.getBytes().length <= 0){
        Toast.makeText(getApplicationContext(), "텍스트를 입력하세요.", Toast.LENGTH_LONG).show();
      }
      else{
        try {
          /*---------------------------------------------------------------------------*
           * 1차 무결성 검증 수행
           *---------------------------------------------------------------------------*/
          AppIron aAppIron = AppIron.getInstance(MainActivity.this);
          return aAppIron.authenticateApp(APPIRON_AUTHCHECK_URL, 30);
        }catch (AppIronException aException) {
          aException.printStackTrace();
          mAppIronException = aException;
        }
      }

      return null;
    }

    @Override
    protected void onPostExecute(AppIronResult pAppIronResult) {
      /*---------------------------------------------------------------------------*
       * 진행 다이얼로그 닫기
       *---------------------------------------------------------------------------*/
      mProgressDialog.hide();

      /*---------------------------------------------------------------------------*
       * 1차 무결성 검증 에러 처리
       *---------------------------------------------------------------------------*/
      if (mAppIronException != null) {
        /*---------------------------------------------------------------------------*
         * 11XX 는 네트워크 에러이므로 다시 접속하도록 구성한다.
         *---------------------------------------------------------------------------*/
        StringBuilder aResultMessage = new StringBuilder();
        if (mAppIronException.getErrorCode() / 100 == 11) {
          aResultMessage.append("네트워크에 연결할 수 없습니다. 잠시 후 다시 시도해주세요.");
        } else {
          aResultMessage.append("무결성 검증에 실패했습니다.");
        }

        aResultMessage.append(String.format("\n[%d][%s]",
                mAppIronException.getErrorCode(),
                mAppIronException.getErrorMessage()));

        /*---------------------------------------------------------------------------*
         * 에러 출력
         *---------------------------------------------------------------------------*/
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("[무결성 검증 실패]");
        alert.setMessage(aResultMessage.toString());
        alert.setCancelable(false);
        alert.setPositiveButton("close", null);
        alert.show();

        return;
      }

      /*---------------------------------------------------------------------------*
       * 1차 무결성 검증의(클라이언트 대 서버 간 검증) 결과값이 정상인 경우, 발급된 세션아이디와 토큰을 가져와
       * 2차 무결성 검증 시(서버 대 서버 간 검증) 사용한다.
       *---------------------------------------------------------------------------*/
      AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
      alert.setTitle("[무결성 검증 성공]");
      alert.setMessage(String.format("세션아이디 [%s]\n토큰 [%s]",
              pAppIronResult.getSessionId(),
              pAppIronResult.getToken()));
      alert.setCancelable(false);
      alert.setPositiveButton("close", null);
      alert.show();
    }
  }
}
