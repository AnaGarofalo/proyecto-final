export const NavigationRoute = {
  DASHBOARD: "/dashboard",
  LOGIN: "/",
  HOMEPAGE: "/homepage",
  EDITPROMPT: "/editprompt",
  DOCUMENTS: "/documents",
  CHATUSERS: "/chatusers",
  USERS: "/users",
} as const;

export type NavigationRoute =
  (typeof NavigationRoute)[keyof typeof NavigationRoute];
