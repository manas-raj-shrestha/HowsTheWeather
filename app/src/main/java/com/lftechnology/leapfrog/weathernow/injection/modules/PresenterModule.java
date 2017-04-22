package com.lftechnology.leapfrog.weathernow.injection.modules;

import com.lftechnology.leapfrog.weathernow.weathercards.WeatherInteractor;
import com.lftechnology.leapfrog.weathernow.weathercards.WeatherInteractorImpl;

import dagger.Module;
import dagger.Provides;


/**
 * Provides methods for creation of presenter instances
 */
@Module
public class PresenterModule {

    @Provides
    public WeatherInteractor getWeatherInteractor() {
        return new WeatherInteractorImpl();
    }

}
