<?php

namespace Database\Seeders;

use App\Enums\RolesEnum;
use App\Models\User;
use Illuminate\Database\Seeder;
use Spatie\Permission\Models\Role;

class AdminUserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $email = env('ADMIN_EMAIL');
        $name = env('ADMIN_NAME');
        $password = env('ADMIN_PASSWORD');

        $admin = User::firstOrCreate(
            ['email' => $email],
            [
                'name' => $name,
                'password' => $password
            ]
        );

        $adminRole = Role::firstOrCreate(['name' => RolesEnum::ADMIN]);

        if (!$admin->hasRole(RolesEnum::ADMIN)) {
            $admin->assignRole($adminRole);
        }
    }
}
