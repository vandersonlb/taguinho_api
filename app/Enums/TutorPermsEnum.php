<?php

namespace App\Enums;

enum TutorPermsEnum: String
{
    case VIEW_OWN_PROFILE = 'view own profile';
    case UPDATE_OWN_PROFILE = 'update own profile';
    case DELETE_OWN_PROFILE = 'delete own profile';
};
