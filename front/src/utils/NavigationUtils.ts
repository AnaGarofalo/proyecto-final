export const NavigationRoute = {
  LOGIN: "/",
  HOMEPAGE: "/homepage",
  EDIT_PROMPT: "/edit-prompt",
  DOCUMENTS: "/documents",
  CHAT_USERS: "/chat-users",
  USERS: "/users",
  TICKET: "/ticket",
  CHAT: "/chat",
} as const;

export type NavigationRoute =
  (typeof NavigationRoute)[keyof typeof NavigationRoute];
