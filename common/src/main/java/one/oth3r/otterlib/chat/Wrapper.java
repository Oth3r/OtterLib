package one.oth3r.otterlib.chat;

public record Wrapper<T, C extends ChatText<T, C>>(ChatText<T,C> front, ChatText<T,C> back) {
}
