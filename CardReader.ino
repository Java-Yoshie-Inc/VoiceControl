#include "SPI.h"
#include "MFRC522.h"

const int pinRST = 5;
const int pinSDA = 53;
MFRC522 mfrc522(pinSDA, pinRST); // Set up mfrc522 on the Arduino

const String correctCard = "1175265213";

int timer = 0;

void setup() {
  SPI.begin(); // open SPI connection
  mfrc522.PCD_Init(); // Initialize Proximity Coupling Device (PCD)
  Serial.begin(9600); // open serial connection
  pinMode(6, OUTPUT);
  pinMode(7, OUTPUT);
}
void loop() {
  if (mfrc522.PICC_IsNewCardPresent()) { // (true, if RFID tag/card is present ) PICC = Proximity Integrated Circuit Card
    if(mfrc522.PICC_ReadCardSerial()) { // true, if RFID tag/card was read
      String cardID = "";
      for (byte i = 0; i < mfrc522.uid.size; ++i) { // read id (in parts)
        cardID += mfrc522.uid.uidByte[i];
      }
      timer = 0;
      if(correctCard.equals(cardID)) {
         Serial.println("true");
         digitalWrite(6, HIGH);
       } else {
        Serial.println("false");
        digitalWrite(7, HIGH);
      }
    }
  } else {
      if(timer % 5 == 0) {
        Serial.println("false");
        digitalWrite(6, LOW);
        digitalWrite(7, LOW);
      }
  }
  timer++;
}
