/** @type {import('tailwindcss').Config} */
import daisyui from 'daisyui';

export default {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {},
  },
  daisyui: {
    themes: [
      {
        moliceiro: {
          "primary": "#2364AA",
          "secondary": "#93BEDF",
          "accent": "#221E22",
          "neutral": "#221E22",
          "base-100": "#FFF9F9",
          "info": "#93BEDF",
          "success": "#36D399",
          "warning": "#FBBD23",
          "error": "#F87272",
        },
      },
    ],
  },
  plugins: [daisyui],
} 