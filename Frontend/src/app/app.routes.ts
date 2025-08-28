import { Routes } from '@angular/router';
import { SignIn } from './pages/sign-in/sign-in';
import { SignUp } from './pages/sign-up/sign-up';
import { TermsAndConditions } from './pages/terms-and-conditions/terms-and-conditions';
import { PrivacyAndPolicy } from './pages/privacy-and-policy/privacy-and-policy';
import { ContractsWorkspace } from './pages/contracts-workspace/contracts-workspace';

export const routes: Routes = [
    {
        path: '',
        component: SignIn,
        title: "Sign In"
    },
    {
        path: 'signUp',
        component: SignUp,
        title: "Sign Up"
    },
    {
        path: 'termsAndConditions',
        component: TermsAndConditions,
        title: "Terms And Conditions"
    },
    {
        path: 'privacyAndPolicy',
        component: PrivacyAndPolicy,
        title: "Privacy And Policy"
    },
    {
        path: 'contractWorkspace',
        component: ContractsWorkspace,
        title: "Contracts Workspace"
    },
    // Default FallBack
    {
        path: '**',
        redirectTo: '',
    },
];
