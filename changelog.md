# b0.2.0.0
Server side Language Reader & Spigot and Paper Ports!

* added a custom language reader
* added spigot port
* added native paper port
* switch logger custom OtterLogger for compatibility between different loaders
* add more overloads to ChatText text() method
* updated CustomFile loading and updating logic
  * added updateFromJSON() for updating the JSON pre-load
  * renamed update() -> updateInstance() for JSON post-load  
* fix fabric main file capitalization