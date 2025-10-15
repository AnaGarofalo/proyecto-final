export const NavigationRoute = {
  LOGIN: "/",
  HOMEPAGE: "/homepage",
  EDIT_PROMPT: "/edit-prompt",
  EDIT_PROMPT_CONFIRMATION: "/edit-prompt/confirmation",
  DOCUMENTS: "/documents",
  CHAT_USERS: "/chat-users",
  USERS: "/users",
} as const;

export type NavigationRoute =
  (typeof NavigationRoute)[keyof typeof NavigationRoute];
