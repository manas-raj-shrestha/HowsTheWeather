package com.lftechnology.leapfrog.weathernow.injection;

import com.lftechnology.leapfrog.weathernow.base.interactor.BaseInteractor;
import com.lftechnology.leapfrog.weathernow.injection.modules.NetworkModule;
import com.lftechnology.leapfrog.weathernow.injection.modules.PresenterModule;
import com.lftechnology.leapfrog.weathernow.update.UpdateService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * links the module with injection classes
 */
@Singleton
@Component(modules = {NetworkModule.class, PresenterModule.class})
public interface WeatherNowComponent {
    void inject(BaseInteractor baseInteractor);

    void inject(UpdateService updateService);
}

