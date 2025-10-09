export const NavigationRoute = {
  LOGIN: "/",
  HOMEPAGE: "/homepage",
  EDITPROMPT: "/editprompt",
  DOCUMENTS: "/documents",
  CHATUSERS: "/chatusers",
  USERS: "/users",
  DOCUMENTSUCCESS: "/documentsuccess",

} as const;

export type NavigationRoute =
  (typeof NavigationRoute)[keyof typeof NavigationRoute];
