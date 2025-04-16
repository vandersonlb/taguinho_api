<?php

namespace Tests\Unit;

use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Illuminate\Support\Facades\Hash;
use Spatie\Permission\Models\Permission;
use Spatie\Permission\Models\Role;
use Tests\TestCase;

#  php artisan test --filter=UserTest
class UserTest extends TestCase
{
    use RefreshDatabase;

    #  php artisan test --filter=UserTest::test_user_creation
    public function test_user_creation(): void
    {
        $name = "John Doe";
        $email = "john_doe@email.com";
        $password = "123ABCabc!@#";
        
        $user = User::create([
            "name" => $name,
            "email" => $email,
            "password" => $password,
        ]);

        $this->assertNotNull($user->id);
        $this->assertIsInt($user->id);
        $this->assertSame($name, $user->name);
        $this->assertSame($email, $user->email);
        $this->assertNotNull($user->created_at);
        $this->assertNotNull($user->updated_at);
    }

    #  php artisan test --filter=UserTest::test_password_encryption
    public function test_password_encryption(): void
    {
        $name = "John Doe";
        $email = "john_doe@email.com";
        $password = "123ABCabc!@#";
        
        $user = User::create([
            "name" => $name,
            "email" => $email,
            "password" => $password,
        ]);

        $this->assertTrue(Hash::check($password, $user->password));
        $this->assertNotSame($password, $user->password);
    }

    # php artisan test --filter=UserTest::test_user_can_have_roles
    public function test_user_can_have_roles(): void
    {
        $user = User::factory()->create();
        Role::create(['name' => 'admin']);

        $user->assignRole('admin');

        $this->assertTrue($user->hasRole('admin'));
    }

    # php artisan test --filter=UserTest::test_user_can_have_permissions
    public function test_user_can_have_permissions(): void
    {
        $perm_1 = Permission::create(['name' => 'view own profile']);
        $perm_2 = Permission::create(['name' => 'update own profile']);
        $perm_3 = Permission::create(['name' => 'delete own profile']);

        $role_admin = Role::create(['name' => 'admin']);
        $role_admin->givePermissionTo([$perm_1, $perm_2, $perm_3]);

        $user = User::factory()->create();
        $user->assignRole('admin');

        $this->assertTrue($user->hasPermissionTo('view own profile'));
        $this->assertTrue($user->hasPermissionTo('update own profile'));
        $this->assertTrue($user->hasPermissionTo('delete own profile'));

    }
}