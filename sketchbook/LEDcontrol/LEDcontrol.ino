/*
  Blink
  Turns on an LED on or off
 */
 
// Pin 13 has an LED connected on most Arduino boards.
// give it a name:
int led = 13;


String inputString = "";         // a string to hold incoming data
boolean stringComplete = false;  // whether the string is complete


// the setup routine runs once when you press reset:
void setup() {
  Serial.begin(9600);

  // initialize the digital pin as an output.
  pinMode(led, OUTPUT);
  digitalWrite(led, LOW);
}


int count = 0;
// the loop routine runs over and over again forever:
void loop() {

    if (Serial.available()) {
        while (Serial.available()) {
            char inChar = (char)Serial.read(); 
            inputString += inChar;
            if (inChar == '\n') {
                stringComplete = true;
                break;
            }
        }
    }
    
    if (stringComplete) {
        // this might represent a command send from the 
        // phone side.  After reading it we would
        // parse it and then schedule the action.

        if(inputString.startsWith("LEDON")){
                  digitalWrite(led, HIGH);
         }else if(inputString.startsWith("LEDOFF")){
                  digitalWrite(led, LOW);
         }


        inputString = "";
        stringComplete = false;

        Serial.println(count++);
        Serial.flush();
    }
 
}
