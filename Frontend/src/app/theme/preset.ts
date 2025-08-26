import Aura from '@primeuix/themes/aura';
import { definePreset } from '@primeuix/themes';

const THEME_BASE_COLOR = 'yellow';
const THEME_SECONDARY_COLOR = '#ffffff';

export const MyPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: `{${THEME_BASE_COLOR}.50}`,
      100: `{${THEME_BASE_COLOR}.100}`,
      200: `{${THEME_BASE_COLOR}.200}`,
      300: `{${THEME_BASE_COLOR}.300}`,
      400: `{${THEME_BASE_COLOR}.400}`,
      500: `{${THEME_BASE_COLOR}.500}`,
      600: `{${THEME_BASE_COLOR}.600}`,
      700: `{${THEME_BASE_COLOR}.700}`,
      800: `{${THEME_BASE_COLOR}.800}`,
      900: `{${THEME_BASE_COLOR}.900}`,
      950: `{${THEME_BASE_COLOR}.950}`,
    },
    colorScheme: {
      light: {
        primary: {
          color: `{${THEME_BASE_COLOR}.500}`,
          inverseColor: THEME_SECONDARY_COLOR,
          hoverColor: `{${THEME_BASE_COLOR}.900}`,
          activeColor: `{${THEME_BASE_COLOR}.500}`,
        },
        highlight: {
          background: `{${THEME_BASE_COLOR}.950}`,
          focusBackground: `{${THEME_BASE_COLOR}.700}`,
          color: THEME_SECONDARY_COLOR,
          focusColor: THEME_SECONDARY_COLOR,
        },
      },
    },
  },
});
