export const NavigationRoute = {
  LOGIN: "/",
  HOMEPAGE: "/homepage",
  EDIT_PROMPT: "/edit-prompt",
  DOCUMENTS: "/documents",
  CHAT_USERS: "/chat-users",
  CHAT_USERS_SUCCESS: "/chat-users/success",
  USERS: "/users",
} as const;

export type NavigationRoute =
  (typeof NavigationRoute)[keyof typeof NavigationRoute];
