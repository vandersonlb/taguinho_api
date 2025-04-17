<?php

namespace Tests\Feature;

use App\Models\User;
use Illuminate\Foundation\Testing\RefreshDatabase;
use Spatie\Permission\Models\Permission;
use Spatie\Permission\Models\Role;
use Tests\TestCase;

#  php artisan test --filter=AuthControllerTest
class AuthControllerTest extends TestCase
{
    use RefreshDatabase;

    protected function setUp(): void
    {
        parent::setUp();
        Role::create(['name' => 'admin']);
        Role::create(['name' => 'tutor']);
    }

    # php artisan test --filter=AuthControllerTest::test_register_new_user_successful
    public function test_register_new_user_successful(): void
    {
        $response = $this->postJson('/api/register', [
            'name' => 'Jane Doe',
            'email' => 'jane@example.com',
            'password' => 'password123',
        ]);

        $response->assertStatus(201);
        $response->assertJsonStructure([
            'success',
            'message',
            'user' => ['id', 'name', 'email', 'created_at', 'updated_at']
        ]);

        $this->assertDatabaseHas('users', ['email' => 'jane@example.com']);
    }

    # php artisan test --filter=AuthControllerTest::test_register_fails_with_invalid_input
    public function test_register_fails_with_invalid_input(): void
    {
        $response = $this->postJson('/api/register', [
            'name' => 'Jane Doe',
            'email' => 'jane.example.com',
            'password' => 'pass',
        ]);

        $response->assertStatus(422);
        $response->assertJsonValidationErrors('email');
        $response->assertJsonValidationErrors('password');

        User::factory()->create(['email' => 'jane@example.com']);

        $response = $this->postJson('/api/register', [
            'name' => 'Jane Doe',
            'email' => 'jane@example.com',
            'password' => 'password',
        ]);

        $response->assertStatus(422);
        $response->assertJsonValidationErrors('email');
    }

    # php artisan test --filter=AuthControllerTest::test_register_fails_when_required_fields_are_missing
    public function test_register_fails_when_required_fields_are_missing(): void
    {
        $response = $this->postJson('/api/register', []);

        $response->assertStatus(422);
        $errors = $response->json('errors');
        $this->assertTrue(isset($errors['name']) || isset($errors['email']) || isset($errors['password']));
    }

    # php artisan test --filter=AuthControllerTest::test_login_successful
    public function test_login_successful(): void
    {
        $user = User::factory()->create(['password' => '12345678']);

        $response = $this->postJson('/api/login', [
            'email' => $user->email,
            'password' => '12345678',
        ]);

        $response->assertStatus(200);
        $response->assertJsonStructure(['auth_token', 'token_type']);
    }

    # php artisan test --filter=AuthControllerTest::test_login_fails_with_invalid_input
    public function test_login_fails_with_invalid_input(): void
    {
        $response = $this->postJson('/api/login', [
            'email' => 'jane.example.com',
            'password' => 'pass',
        ]);

        $response->assertStatus(422);
        $response->assertJsonValidationErrors('email');
        $response->assertJsonValidationErrors('password');
    }

    # php artisan test --filter=AuthControllerTest::test_login_fails_with_wrong_credencials
    public function test_login_fails_with_wrong_credencials(): void
    {
        User::create([
            'name' => 'John Doe',
            'email' => 'john@example.com',
            'password' => 'correctPassword',
        ]);

        $response = $this->postJson('/api/login', [
            'email' => 'john@example.com',
            'password' => 'wrongPassword',
        ]);

        $response->assertStatus(401);
        $response->assertJson(['success' => false]);

        $response = $this->postJson('/api/login', [
            'email' => 'wrogEmail@example.com',
            'password' => 'correctPassword',
        ]);

        $response->assertStatus(401);
        $response->assertJson(['success' => false]);
    }

    # php artisan test --filter=AuthControllerTest::test_login_fails_when_required_fields_are_missing
    public function test_login_fails_when_required_fields_are_missing(): void
    {
        $response = $this->postJson('/api/login', []);

        $response->assertStatus(422);
        $errors = $response->json('errors');
        $this->assertTrue(isset($errors['email']) || isset($errors['password']));
    }

    # php artisan test --filter=AuthControllerTest::test_authenticated_admin_can_access_admin_route
    public function test_authenticated_admin_can_access_admin_route(): void
    {
        $role_admin = Role::where(['name' => 'admin'])->first();
        $user = User::factory()->create(['password' => '12345678']);
        $user->assignRole($role_admin);
        $this->assertTrue($user->hasRole('admin'));

        $token = $this->loginAndGetToken(['email' => $user->email, 'password' => '12345678']);
        $response = $this->withHeaders(['Authorization' => $token])->getJson('/api/admin');

        $response->assertStatus(200);
        $response->assertJsonStructure(['id', 'name', 'email', 'created_at', 'updated_at', 'roles']);
    }

    # php artisan test --filter=AuthControllerTest::test_authenticated_tutor_can_access_tutor_route
    public function test_authenticated_tutor_can_access_tutor_route(): void
    {
        $user = [
            'name' => 'John Doe',
            'email' => 'john_doe@example.com',
            'password' => '123456789',
        ];

        $response = $this->postJson('/api/register', [
            'name' => $user['name'],
            'email' => $user['email'],
            'password' => $user['password'],
        ]);

        $response->assertStatus(201);

        $token = $this->loginAndGetToken(['email' => $user['email'], 'password' => $user['password']]);
        $response = $this->withHeaders(['Authorization' => $token])->getJson('/api/tutor');

        $response->assertStatus(200);
        $response->assertJsonStructure(['id', 'name', 'email', 'created_at', 'updated_at', 'roles']);
    }

    # php artisan test --filter=AuthControllerTest::test_user_with_permission_can_access_route_with_permisson
    public function test_user_with_permission_can_access_route_with_permisson(): void
    {
        $role_tutor = Role::where(['name' => 'tutor'])->first();
        $permission = Permission::create(['name' => 'view own profile']);
        $role_tutor->givePermissionTo($permission);

        $user = [
            'name' => 'John Doe',
            'email' => 'john_doe@example.com',
            'password' => '123456789',
        ];

        $response = $this->postJson('/api/register', [
            'name' => $user['name'],
            'email' => $user['email'],
            'password' => $user['password'],
        ]);

        $response->assertStatus(201);

        $token = $this->loginAndGetToken(['email' => $user['email'], 'password' => $user['password']]);
        $response = $this->withHeaders(['Authorization' => $token])->getJson('/api/tutor/permission');

        $response->assertStatus(200);
        $response->assertJsonStructure(['id', 'name', 'email', 'created_at', 'updated_at', 'permissions', 'roles']);
    }

    # php artisan test --filter=AuthControllerTest::test_authenticated_admin_can_access_route_with_any_permisson
    public function test_authenticated_admin_can_access_route_with_any_permisson(): void
    {
        $role_admin = Role::where(['name' => 'admin'])->first();
        $user = User::factory()->create(['password' => '12345678']);
        $user->assignRole($role_admin);
        $this->assertTrue($user->hasRole('admin'));

        $token = $this->loginAndGetToken(['email' => $user->email, 'password' => '12345678']);
        $response = $this->withHeaders(['Authorization' => $token])->getJson('/api/admin/permission');

        $response->assertStatus(200);
        $response->assertJsonStructure(['id', 'name', 'email', 'created_at', 'updated_at', 'roles']);
    }

    # php artisan test --filter=AuthControllerTest::test_non_admin_cant_access_admin_route
    public function test_non_admins_cant_access_admin_route(): void
    {
        $user = User::factory()->create(['password' => '12345678']);
        $this->assertFalse($user->hasRole('admin'));

        $token = $this->loginAndGetToken(['email' => $user->email, 'password' => '12345678']);
        $response = $this->withHeaders(['Authorization' => $token])->getJson('/api/admin');

        $this->withoutExceptionHandling();

        $response->assertStatus(403);
    }

    # php artisan test --filter=AuthControllerTest::test_non_tutors_cant_access_tutor_route
    public function test_non_tutors_cant_access_tutor_route(): void
    {
        $user = User::factory()->create(['password' => '12345678']);
        $this->assertFalse($user->hasRole('tutor'));

        $token = $this->loginAndGetToken(['email' => $user->email, 'password' => '12345678']);
        $response = $this->withHeaders(['Authorization' => $token])->getJson('/api/tutor');

        $response->assertStatus(403);
    }

    # php artisan test --filter=AuthControllerTest::test_user_without_permission_cant_access_route_with_permissions
    public function test_user_without_permission_cant_access_route_with_permissions(): void
    {
        $user = User::factory()->create(['password' => '12345678']);
        $role_tutor = Role::where(['name' => 'tutor'])->first();
        $permission = Permission::create(['name' => 'view own profile']);
        $role_tutor->givePermissionTo($permission);

        $this->assertFalse($user->hasPermissionTo('view own profile'));

        $token = $this->loginAndGetToken(['email' => $user->email, 'password' => '12345678']);
        $response = $this->withHeaders(['Authorization' => $token])->getJson('/api/tutor/permission');

        $response->assertStatus(403);
    }

    private function loginAndGetToken(array $credentials): string
    {
        $response = $this->postJson('/api/login', $credentials);
        return $response->original['token_type'] . ' ' . $response->original['auth_token'];
    }
}
