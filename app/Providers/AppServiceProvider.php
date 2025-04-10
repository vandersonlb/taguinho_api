<?php

namespace App\Providers;

use App\Enums\RolesEnum;
use Illuminate\Routing\UrlGenerator;
use Illuminate\Support\Facades\Gate;
use Illuminate\Support\ServiceProvider;

class AppServiceProvider extends ServiceProvider
{
    /**
     * Register any application services.
     */
    public function register(): void
    {
        //
    }

    /**
     * Bootstrap any application services.
     */
    public function boot(UrlGenerator $url): void
    {
        if (env('APP_ENV') == 'production') {
            $url->forceScheme('https');
        }

        Gate::before(function ($user) {
            if ($user->hasRole(RolesEnum::ADMIN)) {
                return true;
            }
        });
    }
}
