<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{
    public function login(Request $request)
    {
        $request->validate([
            "email" => "required|string|email ",
            "password" => "required|string|min:8"
        ]);

        $user = User::where('email', $request->email)->first();
        $response = ["success" => false, "message" => "Username or password are incorrect."];
        $response_code = 401;

        if ($user && Hash::check($request->password, $user->password)) {
            $token = $user->createToken('auth_token')->plainTextToken;
            $response = ['auth_token' => $token, 'token_type' => 'Bearer'];
            $response_code = 200;
        } 

        return response()->json($response, $response_code);
    }

    public function register(Request $request)
    {
        $request->validate([
            "email" => "required|string|email|unique:users",
            "name" => "required|string",
            "password" => "required|string|min:8"
        ]);

        $user = User::create($request->all());

        return response()->json([
            "success" => true,
            "message" => "User created successfully.",
            "user" => $user
        ], 201);
    }
}
