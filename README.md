# vantageProCodeRepo

[ebay link of this thing with a decent picture](https://www.ebay.com/itm/Davis-6322-Vantage-Pro2-Vue-Wireless-Integrated-Sensor-Suite-Radiation-Shield/362028158445)

## Note!!

Please don't edit the source code directly.

Please fork it, and submit a pull request.

## How to get the code into your IDE:

1) Fork the code (to your GitHub profile).

2) Go to your Fork, click 'Clone or Download', copy that link

3)Eclipse: File -> Import -> Projects from Git -> Clone URI -> Paste your forked github link into 'URI', type in your GitHub username and password -> select 'store in secure store' -> next -> pick a password for eclipse -> next (you want master) -> change the directory to store the code on your PC if you want, hit next -> next (import existing eclipse projcets) -> next -> -> next... 

## Key:
 :key: = important

:squirrel: = important design decisions

:question: = question

## Links:

Eclipse EGit: [https://youtube.com/watch?v=LPT7v69guVY]

Serialization: [https://youtube.com/watch?v=qo9S2CeoqQE]

Threads: [https://youtube.com/watch?v=qJsZZAa7yJI]

[https://www.baeldung.com/java-concurrent-skip-list-map]

[https://www.baeldung.com/java-serialization]

[https://www.baeldung.com/java-runnable-callable]

[https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/ScheduledExecutorService.html#scheduleAtFixedRate(java.lang.Runnable,%20long,%20long,%20java.util.concurrent.TimeUnit)]

[https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentSkipListMap.html]

[https://docs.oracle.com/javase/8/docs/api/java/time/ZonedDateTime.html]

[https://stackoverflow.com/questions/1085709/what-does-synchronized-mean]

## Questions:

- :question: Do we have to keep track of current draw and battery status (Page 2)?
  - He said we probably don't have to worry about this.

- :question: Do we have to be aware of external temperatures to ensure we are in safe operating temperatures (Page 2)?
  - Probably not.

- :question: Do we have to simulate the various display modes of the console. We transmit the data to the console, which displays it. (Page 3)?
  - This is in part 2 of the assignment.
 
 - :question: This thing has a solar panel. Do we have to simulate that?
   - Probably not.
 
 - :question: This thing communicates to the console (the display) via wireless communication. Do we have to factor in a delay of our output because of the transmission time?
   - Probably not.

## What we're doing:
You are going to develop the core software for the [Wireless Vantage Pro2 Integrated Sensor Suite](https://www.davisinstruments.com/product/wireless-vantage-pro2-integrated-sensor-suite/) (ISS)
(Product number: 6322) manufactured by [Davis Instruments](https://www.davisinstruments.com/).

## What should this software we make do?

- collect data measurements from all attached sensors of the device according to the device specifications (provided on the website), 
- process the sensor data properly to serve (or send) it to the data monitoring devices. 
  - These include [Wireless Vantage Pro2 Console Receivers](https://www.davisinstruments.com/product/vantage-pro2-consolereceiver/?_ga=2.16052982.1932156901.1585547857-930968521.1585547857) (Product number: 6316) and [Wireless Weather Envoy](https://www.davisinstruments.com/product/wireless-weather-envoy/) (Product number: 6316) that transfers the incoming data to a stand-alone weather monitoring application running on your computer.
- Transfer the data between the weather station and receiver or envoy devices on a (wireless) IP network
by serializing the data.

## What features does this Wireless Vantage Pro2™ ISS Product number:6322 possess?
- :key: Detachable _anemometer_ provides wind speed and direction and can be sited up to 40 ft. (12 m) above the other sensors.
- :key: _rain collector_
- :key: _temperature and humidity sensors_ 

## Useful information from: Vantage Pro2 Wireless Stations Specification Sheet.pdf

#### Page 1:

- Wind sensor cable length
  - Cable Length, Anemometer: 40 feet (12 m) (included) 240 feet (73 m) (maximum recommended)
  - Note: Maximum displayable wind decreases as the length of cable increases. At 140’ (42 m) of cable, the maximum wind speed displayed is 135 mph (60 m/s); at 240’ (73 m), the maximum wind speed displayed is 100 mph (34 m/s).
    -  :squirrel: When calculating the max wind speed for our random input generator, we need to factor in the cable length as a field.
   
- Rain Collector Type: Tipping spoon, 0.01" per tip for US versions, 0.2 mm for metric versions, 33.2 in^2 (214 cm ) collection area

#### Page 2:
- Temps:
  - Console Operating Temperature: +32° to +140°F (0° to +60°C)
  - Non-Operating (Storage) Temperature:  +14° to +158°F (-10° to +70°C)

- Batteries:
  - Current Draw: 0.9 mA average, 30 mA peak, (add 120 mA for display lamps, add 0.125 mA for each optional wireless transmitter received by the console) at 4 - 6 VDC
  
  
#### Page 3:
- Display modes.

- Clock 

- Dewpoint (calculated)

#### Page 4:

- Forecast 
  - :question: Does our device support this feature? It requires among other things Barometric Reading & Trend, Humidity, Latitude & Longitude. I'm not sure if our device supports this.
  
- Heat Index (calculated)

- Humidity

#### Page 5:

- Moon Phase 
  - :question: does our device support this?
  
- Rainfall

- Rain Rate

#### Page 6: 

- Sunrise and Sunset

- Temperature

#### Page 8:

- Wind

- Wireless Communications
