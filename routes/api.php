<?php

use App\Enums\TutorPermsEnum;
use App\Http\Controllers\AuthController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::get('/admin', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum', 'role:admin');

Route::get('/admin/permission', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum', 'role:admin', 'permission:'.TutorPermsEnum::VIEW_OWN_PROFILE->value);

Route::get('/tutor', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum', 'role:tutor');

Route::get('/tutor/permission', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum', 'role:tutor', 'permission:'.TutorPermsEnum::VIEW_OWN_PROFILE->value);

Route::post('/login', [AuthController::class, 'login']);
Route::post('/register', [AuthController::class, 'register']);
