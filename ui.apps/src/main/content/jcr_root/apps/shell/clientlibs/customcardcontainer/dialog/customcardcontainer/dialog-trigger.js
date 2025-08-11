(function ($, document) {
  "use strict";

  $(document).on("foundation-form-submitted", function (e) {
    console.log("[CardContainer] 📦 Dialog form submitted");

    const dialog = $(e.target).closest("form");
    const numberOfCards = dialog.find("[name='./numberOfCards']").val();
    const componentPath = dialog.find("[name='./componentPath']").val();

    if (!componentPath) {
      console.warn("[CardContainer] ❌ No component path found");
      return;
    }

    const url = componentPath.endsWith(".dynamic.json")
      ? componentPath
      : componentPath + ".dynamic.json";

    $.ajax({
      type: "POST",
      url: url,
      data: { numberOfCards: numberOfCards },
      headers: { "Referer": window.location.href },
      xhrFields: { withCredentials: true },
      success: function () {
        if (window.Granite && Granite.author && Array.isArray(Granite.author.editables)) {
          const editable = Granite.author.editables.find(function (ed) {
            return ed.path === componentPath;
          });

          if (editable) {
            console.log("[CardContainer] ♻ Refreshing component in Author mode");
            Granite.author.refreshComponent(editable);
          } else if (
            typeof Granite.author.Editor === "object" &&
            typeof Granite.author.Editor.refresh === "function"
          ) {
            console.log("[CardContainer] ⚠ Editable not found, calling Granite.author.Editor.refresh()");
            Granite.author.Editor.refresh();
          } else {
            console.warn("[CardContainer] ⚠ Editable not found, reloading page");
            location.reload();
          }
        } else if (
          typeof Granite.author === "object" &&
          Granite.author.Editor &&
          typeof Granite.author.Editor.refresh === "function"
        ) {
          console.log("[CardContainer] ♻ Using Granite.author.Editor.refresh()");
          Granite.author.Editor.refresh();
        } else {
          console.log("[CardContainer] 🔄 Fallback to full page reload");
          location.reload();
        }
      },
      error: function (xhr, status, error) {
        console.error("[CardContainer] ❌ Servlet call failed:", status, error, xhr.responseText);
      },
    });
  });
})(jQuery, document);
