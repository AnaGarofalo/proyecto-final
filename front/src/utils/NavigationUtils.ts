export const NavigationRoute = {
  LOGIN: "/",
  HOME_PAGE: "/home-page",
  EDIT_PROMPT: "/edit-prompt",
  DOCUMENTS: "/documents",
  CHAT_USERS: "/chat-users",
  USERS: "/users",
} as const;

export type NavigationRoute =
  (typeof NavigationRoute)[keyof typeof NavigationRoute];
