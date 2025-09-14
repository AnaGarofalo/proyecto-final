export const NavigationRoute = {
  DASHBOARD: "/dashboard",
  LOGIN: "/",
} as const;

export type NavigationRoute = typeof NavigationRoute[keyof typeof NavigationRoute];