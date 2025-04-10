<?php

use Illuminate\Database\Migrations\Migration;
use Spatie\Permission\Models\Role;
use Spatie\Permission\Models\Permission;

return new class extends Migration
{
    private $adminPermissions = ['create user', 'view user', 'update user', 'delete user', 'list users'];
    private $userPermissions = ['view own profile', 'update own profile', 'delete own profile'];
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        $adminRole = Role::create(['name' => 'admin']);
        $userRole = Role::create(['name' => 'user']);

        $allPermissions = array_merge($this->adminPermissions, $this->userPermissions);
        foreach ($allPermissions as $permission) {
            Permission::firstOrCreate(['name' => $permission, 'guard_name' => env('AUTH_GUARD')]);
        }

        $adminRole->givePermissionTo($this->adminPermissions);
        $userRole->givePermissionTo($this->userPermissions);
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Role::whereIn('name', ['admin', 'user'])->delete();
        $allPermissions = array_merge($this->adminPermissions, $this->userPermissions);
        Permission::whereIn('name', $allPermissions)->delete();
    }
};
