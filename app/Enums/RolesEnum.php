<?php

namespace App\Enums;

enum RolesEnum: String
{
    case ADMIN = 'admin';
    case TUTOR = 'tutor';

    public function label(): string
    {
        return match ($this) {
            static::ADMIN => 'Administrator',
            static::TUTOR => 'Tutor',
        };
    }
};
