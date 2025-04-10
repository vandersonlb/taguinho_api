<?php

namespace Database\Seeders;

use App\Enums\RolesEnum;
use App\Enums\TutorPermsEnum;
use Illuminate\Database\Seeder;
use Spatie\Permission\Models\Permission;
use Spatie\Permission\Models\Role;
use Spatie\Permission\PermissionRegistrar;

class RolesAndPermsSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        app()[PermissionRegistrar::class]->forgetCachedPermissions();

        Permission::firstOrCreate(['name' => TutorPermsEnum::VIEW_OWN_PROFILE, 'guard_name' => env('AUTH_GUARD')]);
        Permission::firstOrCreate(['name' => TutorPermsEnum::UPDATE_OWN_PROFILE, 'guard_name' => env('AUTH_GUARD')]);
        Permission::firstOrCreate(['name' => TutorPermsEnum::DELETE_OWN_PROFILE, 'guard_name' => env('AUTH_GUARD')]);

        $tutorRole = Role::firstOrCreate(['name' => RolesEnum::TUTOR]);
        $tutorRole->givePermissionTo(TutorPermsEnum::VIEW_OWN_PROFILE);
        $tutorRole->givePermissionTo(TutorPermsEnum::UPDATE_OWN_PROFILE);
        $tutorRole->givePermissionTo(TutorPermsEnum::DELETE_OWN_PROFILE);
    }
}
