#include <Arduino.h>
#include <Keypad.h>
#include <SoftwareSerial.h>
#include <String.h>
#include <Wire.h>
#include <LiquidCrystal_I2C.h>

//Config ESP
//const char wifiName = {'E'};
SoftwareSerial wifi(12, 11);
char responseBuffer[50];
///////////////

//Config KEYPAD
const byte rows = 4; //four rows
const byte cols = 4; //three columns
char keys[rows][cols] = {
  {'1','2','3', 'A'},
  {'4','5','6', 'B'},
  {'7','8','9', 'C'},
  {'*','0','#', 'D'},
};

byte rowPins[rows] = {4, 5, 6, 8}; //connect to the row pinouts of the keypad
byte colPins[cols] = {A3, A2, A1, A0}; //connect to the column pinouts of the keypad

Keypad keypad = Keypad( makeKeymap(keys), colPins, rowPins ,cols, rows );
char men[15];
char pass[9] = {'1', '2', '3', '4', '\0'};
char masterPass[9] = {'9', '8', '7', '6', '5', '4', '3', '2', '\0'};

int i = 0;
int blockcounter = 0;
int changepass;
int newPass = 0;
String data, data1, data2, data3;
String datalength;
String peticion;
String envio;
String test;
String dataGET, datalengthGT, peticionGET, dataPUT, datalengthPT,peticionPUT;


LiquidCrystal_I2C lcd(0x3F,16,2);

///////////////////////////////////////////////////////////////////////////////


void wifiInit(){
  String SSID = "Wifi Casa";
  String PASS = "pilarluisivanruben1492";

  //Reinicio
  for(int i = 0; i<20;i++){
      wifi.println("AT+RST");
    if(wifi.find("OK")){
      Serial.println("Restart OK");
      break;
    }else{
      Serial.println("Bad restart");
      delay(200);
    }
  }


  //CIPMUX
      for(int i = 0; i<20;i++){
        wifi.println("AT+CIPMUX=0");
          if(wifi.find("OK")){
            Serial.println("CIPMUX OK");
            break;
          }else{
            Serial.println("CIPMUX bad");
            delay(200);
          }
      }


      //CWMODE
      for(int i = 0; i<4;i++){
        wifi.println("AT+CWMODE=1");
        if(wifi.find("OK") || wifi.find("no change")){
          Serial.println("CWMODE OK");
          break;
        }else{
          Serial.println("CWMODE bad");
          delay(200);
        }
      }

//CWJAP
  for(int i = 0; i<4;i++){
    wifi.println('AT+CWJAP="S8 Manu","27121996"');
    //wifi.println("AT+CWJAP=\"Wifi Casa\", \"pilarluisivanruben1492\"");
    delay(2000);
    if(wifi.find("WIFI GOT IP") || wifi.find("OK")){
      Serial.println("Conexion wifi OK");
      break;
    }else{
      Serial.println("Conexion wifi bad");
      delay(200);
      if(i == 3){
        Serial.println("IMPOSSIBLE TO CONNECT");
      }
    }
  }




  for(int i = 0; i<10;i++){
    wifi.println("AT+CIPSTART=\"TCP\",\"192.168.43.170\",8083");
    //wifi.println("AT+CIPSTART=\"TCP\",\"192.168.0.158\",8083");
    //wifi.println('AT+CIPSTART=?');
    if(wifi.find("OK")){
        Serial.println("TCP OK");
        break;
      }else{
        Serial.println("TCP bad");
        delay(200);
      }
  }

  /*for(int i = 0; i<10;i++){
    wifi.println("AT+CIPMODE=0");
    //wifi.println('AT+CIPSTART=?');
    if(wifi.find("OK")){
        Serial.println("CIPMODE OK");
        break;
      }else{
        Serial.println("CIPMODE bad");
        delay(200);
      }
  }*/

  //
  data1 = 'PUT 192.168.43.170:8083/api/door HTTP/1.1\r\n';
  data2 = '{"id":5,"doorState":0,"doorAddress":"Arduino","doorPass":"9999","doorAdmin":"AAAA"}';
  datalength = data2.length();
  data = data1 + ' Content-Length:' + datalength + data2 + '\r\n';
  peticion = 'AT+CIPSEND=' + data2.length();

  dataGET = 'GET /api/door/1 HTTP/1.1\r\n';
  datalengthGT = dataGET.length();
  peticionGET = 'AT+CIPSEND='+ datalengthGT;

  dataPUT = 'PUT /api/door HTTP/1.1\r\n\r\n {"id":7,"doorState":0,"doorAddress":"Arduino","doorPass":"9999","doorAdmin":"AAAA"}';
  datalengthPT = dataPUT.length();
  //peticionPUT = 'AT+CIPSEND='+ datalengthPT;
  peticionPUT = 'AT+CIPSEND='+ datalengthGT;

  for(i=0;i<10;i++){
    wifi.println(peticionGET);

    delay(500);

    if(wifi.find('>')){
      Serial.println("OK FOUND");
    }
      Serial.println("Sending...");
      lcd.print("Enviando...");
      delay(100);
      lcd.clear();
      wifi.println(dataGET);
      if( wifi.find("SEND OK")) {
         Serial.println("Packet sent");
         lcd.clear();

      }else{
        Serial.println("SEND BAD");
      }
  //  }else{
      //Serial.println("Response not found");
    //}
  }

}


///////////////////////////////////////////////////////////////////////////////

void setup() {

  Serial.begin(9600);
  Serial.setTimeout(500);
  wifi.begin(9600);
  wifi.setTimeout(500);

  lcd.init(A5,A4);
  lcd.backlight();

  //wifiInit();
}


void whiteFlash(){
  analogWrite(9, 255);
  analogWrite(10, 255);
  analogWrite(7, 255);
  delay(100);
  analogWrite(9, 0);
  analogWrite(10, 0);
  analogWrite(7, 0);
}

void blueFlash(){
  analogWrite(9, 0);
  analogWrite(10, 0);
  analogWrite(7, 255);
  delay(100);
  analogWrite(9, 0);
  analogWrite(10, 0);
  analogWrite(7, 00);
}

void redFlash(){
  analogWrite(9, 255);
  analogWrite(10, 0);
  analogWrite(7, 0);
  delay(100);
  analogWrite(9, 0);
  analogWrite(10, 0);
  analogWrite(7, 00);
}

void red(){
  analogWrite(9, 255);
  analogWrite(10, 0);
  analogWrite(7, 0);
}

void green(){
  analogWrite(9, 0);
  analogWrite(10, 255);
  analogWrite(7, 0);
}

void blue(){
  analogWrite(9, 0);
  analogWrite(10, 0);
  analogWrite(7, 255);
}


void loop() {

  char key = keypad.getKey();


if (key != NO_KEY) {
  if(blockcounter >=3){
    int flash = 100;
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("Bloqueda");
    while(flash < 3000){
    redFlash();
    delay(100);
    flash = flash + 100;
    }
    lcd.clear();
    blockcounter = 0;
  }else if (key != '*' && key != '#'){
    blueFlash();
    men[i] = key;
    if(i==0){
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Password:");
    }
    lcd.setCursor(i,1);
    lcd.print('*');

    i++;
  }else if (key == '*' && newPass == 1){ //NUEVA PASS
    i = 0;
    memset(pass, 0, sizeof(pass));
    strcpy(pass, men);
    memset(men, 0, sizeof(men));
    Serial.print("Insert new pass: ");
    Serial.println(pass);

    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("Pass changed");
    newPass = 2;

    whiteFlash();
    delay(100);
    whiteFlash();
    delay(100);
    whiteFlash();

    delay(500);

    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("New masterpass:");

  }else if (key == '*' && newPass == 2){ //NUEVA MASTER PASS
    i = 0;
    memset(masterPass, 0, sizeof(masterPass));
    strcpy(masterPass, men);
    memset(men, 0, sizeof(men));
    Serial.print("New masterpass: ");
    Serial.println(pass);

    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("New masterpassOK");

    newPass = 0;

    whiteFlash();
    delay(100);
    whiteFlash();
    delay(100);
    whiteFlash();
  }else if (key == '*'){
    i = 0;
    if(strcmp(men, pass) == 0){ //Equals zero means that both strings are equals
      blockcounter = 0;
      green();
      Serial.print("Password OK, door open: ");
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Door opened.");

      digitalWrite(13, HIGH);
      delay(1000);
      digitalWrite(13, LOW);
      lcd.clear();

    }else if(strcmp(men, masterPass) == 0){ //Master Pass OK
      blockcounter = 0;
      whiteFlash();
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Master Pass OK");
      delay(500);
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Insert new pass:");
      newPass = 1;
    }else{ // Fallo
      blockcounter++;
      redFlash();
      Serial.print("Password error: ");
      lcd.clear();
      lcd.setCursor(0,0);
      lcd.print("Password error.");
    }
    Serial.println(men);
    memset(men, 0, sizeof(men));

  }else if (key == '#'){ //Reseteo del password
    i = 0;
    Serial.println("Password reset.");
    lcd.clear();
    lcd.setCursor(0,0);
    lcd.print("Password reset.");
    memset(men, 0, sizeof(men));
    redFlash();


  }
}
}
