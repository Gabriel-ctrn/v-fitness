/**
 * Below are the colors that are used in the app. The colors are defined in the light and dark mode.
 * There are many other ways to style your app. For example, [Nativewind](https://www.nativewind.dev/), [Tamagui](https://tamagui.dev/), [unistyles](https://reactnativeunistyles.vercel.app), etc.
 */

const darkGreen = "#2C2C2C";
const lightGreen = "#08A47C";
const almostWhite = "#F1F6F2";

export const Colors = {
  light: {
    text: darkGreen,
    background: almostWhite,
    tint: lightGreen,
    icon: darkGreen,
    tabIconDefault: darkGreen,
    tabIconSelected: lightGreen,
  },
  dark: {
    text: almostWhite,
    background: darkGreen,
    tint: lightGreen,
    icon: almostWhite,
    tabIconDefault: almostWhite,
    tabIconSelected: lightGreen,
  },
};
