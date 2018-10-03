package com.jetopto.bsm.presenter.interfaces;

public interface ContentObserveMvpView  {

    String TYPE_RING = "volume_ring_speaker";
    String TYPE_MUSIC = "volume_music_speaker";
    String TYPE_ALARM = "volume_alarm_speaker";
    String TYPE_BRIGHTNESS_MODE = "screen_brightness_mode";
    String TYPE_BRIGHTNESS_LEVEL = "screen_brightness";

    void onChanged(String type);

}
